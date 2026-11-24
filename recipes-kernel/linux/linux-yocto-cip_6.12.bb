# Copyright (C) 2026 Hirotaka Motai <hirotaka.motai@miraclelinux.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux kernel for the Civil Infrastructure Platform"
DESCRIPTION = "Linux kernel from the Civil Infrastructure Platform (CIP) project, using Yocto kernel tooling."
HOMEPAGE = "https://www.kernel.org/"
# CIP does not have a bug tracking system, see
# https://cip-documents.readthedocs.io/en/latest/process/Security_issues_handling.html
BUGTRACKER = "https://bugzilla.kernel.org/"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
CVE_PRODUCT = "linux_kernel"

# linux-yocto-custom.bb:
#
#   kernel classes to apply a subset of yocto kernel management to git
#   managed kernel repositories.
#
#   To use linux-yocto-custom in your layer, copy this recipe (optionally
#   rename it as well) and modify it appropriately for your machine. i.e.:
#
#     COMPATIBLE_MACHINE_yourmachine = "yourmachine"
#
#   You must also provide a Linux kernel configuration. The most direct
#   method is to copy your .config to files/defconfig in your layer,
#   in the same directory as the copy (and rename) of this recipe and
#   add file://defconfig to your SRC_URI.
#
#   To use the yocto kernel tooling to generate a BSP configuration
#   using modular configuration fragments, see the yocto-bsp and
#   yocto-kernel tools documentation.
#
# Warning:
#
#   Building this example without providing a defconfig or BSP
#   configuration will result in build or boot errors. This is not a
#   bug.
#
# Notes:
#
#   patches: patches can be merged into the source git tree itself,
#            added via the SRC_URI, or controlled via a BSP
#            configuration.
#
#   defconfig: When a defconfig is provided, the linux-yocto configuration
#              uses the filename as a trigger to use a 'allnoconfig' baseline
#              before merging the defconfig into the build.
#
#              If the defconfig file was created with make_savedefconfig,
#              not all options are specified, and should be restored with their
#              defaults, not set to 'n'. To properly expand a defconfig like
#              this, specify: KCONFIG_MODE="--alldefconfig" in the kernel
#              recipe.
#
#   example configuration addition:
#            SRC_URI += "file://smp.cfg"
#   example patch addition (for kernel v4.x only):
#            SRC_URI += "file://0001-linux-version-tweak.patch"
#   example feature addition (for kernel v4.x only):
#            SRC_URI += "file://feature.scc"
#

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION = "6.12.94"
LINUX_VERSION_EXTENSION:append = "-cip26"
LINUX_CIP_BRANCH ?= "linux-6.12.y-cip"
LINUX_CIP_KMETA_BRANCH = "yocto-6.12"
KMETA = "kernel-meta"

PV = "${LINUX_VERSION}-cip26"

SRC_URI += "git://git.kernel.org/pub/scm/linux/kernel/git/cip/linux-cip.git;branch=${LINUX_CIP_BRANCH};protocol=https \
            git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${LINUX_CIP_KMETA_BRANCH};destsuffix=${KMETA};protocol=https \
            "
SRC_URI:append:qemux86-64 = " file://qemu-x86_64-amdgpu.cfg"

SRCREV_meta = "01498af34f6fe82c08fa6eae50a7eff1904a3968"
SRCREV = "5ff8c31327ef74b3d242e05452935ef6c6fea3d6"

# Override COMPATIBLE_MACHINE to include your machine in a copy of this recipe
# file. Leaving it empty here ensures an early explicit build failure.
COMPATIBLE_MACHINE = "^(qemux86|qemux86-64|qemuarm|qemuarmv5|qemuarm64|qemuppc|qemumips|rpi)$"
