# Copyright (C) 2023 Hirotaka Motai <hirotaka.motai@miraclelinux.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "An example kernel recipe that uses the linux-yocto and oe-core"
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
#
# Notes:
#
#   patches: patches can be merged into to the source git tree itself,
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

SRC_URI += " \
    git://git.kernel.org/pub/scm/linux/kernel/git/cip/linux-cip.git;branch=${LINUX_CIP_BRANCH};protocol=https \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${LINUX_CIP_KMETA_BRANCH};destsuffix=${KMETA} \
    "

LINUX_VERSION = "6.1.59"
LINUX_VERSION_EXTENSION:append = "-cip8"
LINUX_CIP_BRANCH ?= "linux-6.1.y-cip"

LINUX_CIP_KMETA_BRANCH = "yocto-6.1"
SRCREV_meta = "a7bfe6ed5d665b094b3e2d30c4cd94dece2c75f3"
KMETA = "kernel-meta"

SRCREV = "5ac26de5cee674d669fd90d11fb844eaf356ec9c"

PV = "${LINUX_VERSION}-cip8"

# Override COMPATIBLE_MACHINE to include your machine in a copy of this recipe
# file. Leaving it empty here ensures an early explicit build failure.
COMPATIBLE_MACHINE = "^(qemux86|qemux86-64|qemuarm|qemuarmv5|qemuarm64|qemuppc|qemumips|rpi)$"

## need to update license checksum to linux-6.1
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

