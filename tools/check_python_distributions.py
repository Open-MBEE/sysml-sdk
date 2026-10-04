"""Validate the Python artifacts before registry publication."""

import os
from email.parser import BytesParser
from pathlib import Path
import sys
import tarfile
import tomllib
import zipfile


def main():
    project = tomllib.loads(Path('pyproject.toml').read_text(encoding='utf-8'))['project']
    version = project['version']
    if os.environ.get('GITHUB_REF', '').startswith('refs/tags/'):
        if os.environ['GITHUB_REF_NAME'] != f'v{version}':
            raise SystemExit('Release tag must match the version in pyproject.toml')
    dist = Path(sys.argv[1])
    wheels = sorted(dist.glob('*.whl'))
    sources = sorted(dist.glob('*.tar.gz'))
    expected = {'win_amd64', 'manylinux_2_28_x86_64', 'macosx_11_0_arm64', 'macosx_10_12_x86_64'}
    if len(wheels) != 4 or {p.stem.rsplit('-', 1)[1] for p in wheels} != expected:
        raise SystemExit('Expected exactly one native wheel for each of the four supported platforms')
    if len(sources) != 1:
        raise SystemExit('Expected exactly one source distribution')
    for path in wheels + sources:
        if path.suffix == '.whl':
            with zipfile.ZipFile(path) as archive:
                [metadata] = [n for n in archive.namelist() if n.endswith('.dist-info/METADATA')]
                native = [n for n in archive.namelist() if n.startswith('sysml/_native/') and n.endswith(('.dll', '.so', '.dylib'))]
                if len(native) != 1:
                    raise SystemExit(f'{path}: expected one bundled native library')
                data = archive.read(metadata)
        else:
            with tarfile.open(path) as archive:
                [metadata] = [m for m in archive.getmembers() if m.name.count('/') == 1 and m.name.endswith('/PKG-INFO')]
                data = archive.extractfile(metadata).read()
        metadata = BytesParser().parsebytes(data)
        if metadata['Name'] != 'sysml' or metadata['Version'] != version:
            raise SystemExit(f'{path}: package name or version differs from sysml {version}')
        print(f'Validated {path.name}')


if __name__ == '__main__':
    main()
