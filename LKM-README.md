# 大肥鱼KSU LKM

配合大肥鱼KSU 管理器使用的自定义 `kernelsu.ko`。

## 支持范围

- `android15-6.6`
- `android16-6.12`
- `android17-6.18`

构建工作流：`.github/workflows/build-baifan-lkm.yml`  
KernelSU 源码：v3.3.1（`KSU_VERSION=32680`，UAPI 5）

## 为什么需要配套 LKM

管理器使用自定义 v2-only 签名。KernelSU 内核会根据管理器签名决定是否信任当前 APK：

- 官方 **bundled LKM** 只信任官方管理器签名
- 使用本仓库的 LKM 后，内核会信任大肥鱼KSU 的自定义签名

## 管理器签名

```text
APK signing: v2-only
Certificate DER: 825 bytes (0x0339)
Certificate SHA-256: bad08be1f8e813067892adca0e70452cd645ba31e5ed15bec35e60fa430f2f48
Manager package: me.weishu.kernelsu
```

## 构建参数

```make
KSU_GIT_VERSION=2680
KSU_VERSION=32680
KSU_EXPECTED_SIZE=0x0339
KSU_EXPECTED_HASH=bad08be1f8e813067892adca0e70452cd645ba31e5ed15bec35e60fa430f2f48
KSU_MANAGER_PACKAGE=me.weishu.kernelsu
```

## 产物

```text
kernelsu-android15-6.6-baifan.ko
kernelsu-android16-6.12-baifan.ko
kernelsu-android17-6.18-baifan.ko
```

对应 Release：

<https://github.com/NekoWannn/baifan-ksu-lkm/releases/tag/v3.3.1-baifan.13>
