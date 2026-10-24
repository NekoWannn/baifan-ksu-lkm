# Baifan KernelSU LKM

Private build of `kernelsu.ko` for the Baifan-themed manager.

## Target

- KMI: `android15-6.6`
- KernelSU source: v3.3.0
- Manager package: `me.weishu.kernelsu`
- Build workflow: `.github/workflows/build-baifan-lkm.yml`

## Manager signature

- APK signing: v2-only
- Certificate: DER 825 bytes (`0x0339`)
- SHA-256: `bad08be1f8e813067892adca0e70452cd645ba31e5ed15bec35e60fa430f2f48`

## Embedded signature values

```make
KSU_EXPECTED_SIZE := 0x0339
KSU_EXPECTED_HASH := bad08be1f8e813067892adca0e70452cd645ba31e5ed15bec35e60fa430f2f48
KSU_EXPECTED_SIZE2 := 0x033b
KSU_EXPECTED_HASH2 := c371061b19d8c7d7d6133c6a9bafe198fa944e50c1b31c9d8daa8d7f1fc2d2d6
KSU_MANAGER_PACKAGE := me.weishu.kernelsu
```

The second pair keeps the official manager signature accepted as a fallback.

## Output

GitHub Actions artifact:

```text
kernelsu-android15-6.6-baifan.ko
```
