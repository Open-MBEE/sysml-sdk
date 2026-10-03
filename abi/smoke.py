"""End-to-end check of the binding library from Python.

Usage: python smoke.py <model.sysml>
Loads the model, walks the roots, reads names and features, and asserts that a member of another
metaclass, a refused operation and a bogus handle each fail with their status. The library is
target/release beside this file, or SYSMLV2_ABI.
"""
import ctypes as C, sys
import os
HERE = os.path.dirname(os.path.abspath(__file__))
NAME = {"win32": "sysmlv2_abi.dll", "darwin": "libsysmlv2_abi.dylib"}.get(sys.platform, "libsysmlv2_abi.so")
lib = C.CDLL(os.environ.get("SYSMLV2_ABI") or os.path.join(HERE, "target", "release", NAME))

class Str(C.Structure):     _fields_ = [("ptr", C.c_void_p), ("len", C.c_size_t), ("present", C.c_bool), ("owner", C.c_void_p)]
class Handles(C.Structure): _fields_ = [("items", C.POINTER(C.c_uint64)), ("len", C.c_size_t), ("owner", C.c_void_p)]
class Ref(C.Structure):     _fields_ = [("present", C.c_bool), ("value", C.c_uint64)]
class Bool(C.Structure):    _fields_ = [("present", C.c_bool), ("value", C.c_bool)]
class LoadOptions(C.Structure):
    _fields_ = [("paths", C.POINTER(Str)), ("paths_len", C.c_size_t), ("source_names", C.POINTER(Str)),
                ("source_texts", C.POINTER(Str)), ("sources_len", C.c_size_t), ("library_dir", Str)]
STATUS = ["OK","NOT_IMPLEMENTED","NOT_APPLICABLE","INVALID_HANDLE","INVALID_ARG","LOAD_FAILED","INTERNAL"]

def s_of(b: bytes):
    buf = C.create_string_buffer(b, len(b)); keep.append(buf)
    return Str(C.cast(buf, C.c_void_p), len(b), True, None)
keep = []
def take_str(st: Str):
    v = C.string_at(st.ptr, st.len).decode() if st.present and st.ptr else None
    lib.sysmlv2_free(st.owner); return v
def take_handles(hs: Handles):
    v = [hs.items[i] for i in range(hs.len)]; lib.sysmlv2_free(hs.owner); return v

lib.sysmlv2_free.argtypes = [C.c_void_p]
lib.sysmlv2_names_digest.restype = C.c_void_p; lib.sysmlv2_names_digest_len.restype = C.c_size_t
print("abi version:", lib.sysmlv2_abi_version(), "| digest:", C.string_at(lib.sysmlv2_names_digest(), lib.sysmlv2_names_digest_len()).decode()[:12])

path = s_of(sys.argv[1].encode())
paths = (Str * 1)(path)
opts = LoadOptions(paths, 1, None, None, 0, Str(None, 0, False, None))
sess = C.c_void_p()
st = lib.sysmlv2_session_open(C.byref(opts), C.byref(sess)); print("open:", STATUS[st]); assert st == 0

def call(name, h, out_t, *args):
    fn = getattr(lib, "sysmlv2_" + name); out = out_t()
    st = fn(sess, C.c_uint64(h), *args, C.byref(out)); return STATUS[st], out

roots = Handles(); lib.sysmlv2_session_roots(sess, C.byref(roots)); roots = take_handles(roots)
print("roots:", len(roots))
for r in roots:
    st, mc = call("metaclass", r, Str); st2, nm = call("element_declared_name", r, Str)
    print(f"  root h={r} {take_str(mc)} '{take_str(nm)}'")
    st, members = call("namespace_owned_member", r, Handles)
    for m in take_handles(members)[:6]:
        _, mc = call("metaclass", m, Str); _, qn = call("element_qualified_name", m, Str)
        _, feats = call("type_owned_feature", m, Handles); nf = len(take_handles(feats))
        _, comp = call("feature_is_composite", m, Bool)
        print(f"    h={m:<3} {take_str(mc):<18} {take_str(qn)!s:<32} ownedFeature={nf} isComposite={STATUS[_ if isinstance(_,int) else 0]}" if False else
              f"    h={m:<3} {take_str(mc):<18} {take_str(qn)!s:<32} ownedFeature={nf}")
    break

def last_error():
    err = Str(); lib.sysmlv2_last_error(sess, C.byref(err)); return take_str(err)

print("--- refusals ---")
st, out = call("feature_featuring_type", roots[0], Handles)
print("Feature::featuringType on a Namespace ->", st, "|", last_error()); assert st == "NOT_APPLICABLE"
# An operation the SysML Toolkit has no body for yet is refused by it, with its reason.
st, out = call("element_path", roots[0], Str)
print("Element::path (operation) ->", st, "|", last_error()); assert st == "NOT_IMPLEMENTED"
st, out = call("element_owner", 999999, Ref); print("bogus handle ->", st); assert st == "INVALID_HANDLE"

first = take_handles((lambda hs: (lib.sysmlv2_namespace_owned_member(sess, C.c_uint64(roots[0]), C.byref(hs)), hs)[1])(Handles()))[0]
_, qn = call("element_qualified_name", first, Str); qn = take_str(qn)
out = Ref(); st = lib.sysmlv2_session_resolve(sess, C.byref(s_of(qn.encode())), C.byref(out))
print(f"session_resolve({qn!r}) ->", STATUS[st], out.value); assert st == 0 and out.present and out.value == first
lib.sysmlv2_session_close(sess); print("closed OK")
