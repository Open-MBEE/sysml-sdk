# Publishing sysml

The Python distribution and import name are both `sysml`. Publication runs from
`Open-MBEE/sysml-sdk`, using `.github/workflows/publish.yml`.

## One-time registry setup

Create GitHub environments named `pypi` and `testpypi` in `Open-MBEE/sysml-sdk`.
Configure a trusted publisher separately on PyPI and TestPyPI:

| Field | PyPI | TestPyPI |
|---|---|---|
| Project name | `sysml` | `sysml` |
| Repository owner | `Open-MBEE` | `Open-MBEE` |
| Repository name | `sysml-sdk` | `sysml-sdk` |
| Workflow filename | `publish.yml` | `publish.yml` |
| Environment | `pypi` | `testpypi` |

For a new project use a pending publisher in the registry's account publishing
settings; for an existing project its owner must add the publisher in the project's
publishing settings. The project name must be available or controlled by the publisher's
account. No API token secrets are required.

See [PyPI trusted publisher setup](https://docs.pypi.org/trusted-publishers/adding-a-publisher/)
and [creating a project](https://docs.pypi.org/trusted-publishers/creating-a-project-through-oidc/).

## TestPyPI

Run **Publish sysml** manually in GitHub Actions, selecting the branch or tag to test.
Every manual run targets TestPyPI. Each published version must be unique; update
`pyproject.toml` before uploading a version already present on that registry.

```sh
python -m pip install --index-url https://test.pypi.org/simple/ sysml==0.1.0
```

## PyPI

Set the version in `pyproject.toml`, keep the other language versions and changelog
consistent, and push its matching tag (for example `v0.1.0`). Existing CI builds and
checks all release assets and creates a draft GitHub release. Publish that draft to
trigger **Publish sysml**, which rebuilds and validates before uploading to PyPI.
Publishing does not create another draft release.

After committing the release version and changelog, create and push the tag:

```sh
git tag v0.1.0
git push origin v0.1.0
```

Wait for the tag's **CI** run to succeed, open the draft under the repository's
**Releases**, and select **Publish release**. Watch **Publish sysml** in **Actions**;
its `pypi` job uploads the distributions. A manual workflow run uploads to TestPyPI,
even when you select a release tag.

After publication, verify installation in a fresh virtual environment:

```sh
python -m venv .venv-pypi-check
# Activate: source .venv-pypi-check/bin/activate (Linux/macOS)
# or .venv-pypi-check\Scripts\Activate.ps1 (PowerShell)
python -m pip install sysml==0.1.0
python -c "from importlib.metadata import version; from sysml import Model; print(version('sysml'))"
```

Replace `0.1.0` with the release version. Uploaded distribution files cannot be
overwritten; use a new version when correcting a published package.

The shared packaging workflow reuses the full CI pipeline, including native backend
and installed-package checks. It publishes exactly four native platform wheels and
one source distribution. Twine validates metadata, package names and versions are
checked, release tags must match `pyproject.toml`, and the source distribution is
rebuilt and imported outside the checkout before publication. Source installations
provide the payload backend; use a supported platform wheel for the bundled Toolkit
backend. The standard library remains a separate GitHub release asset.
