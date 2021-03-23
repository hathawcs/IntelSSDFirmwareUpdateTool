SUMMARY = "Multi-purpose linux bootloader"
HOMEPAGE = "http://www.syslinux.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://README;md5=cb979cb775d62f7a0c4c2f3c7ccf5bcd"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS = "nasm-native util-linux e2fsprogs"

SRC_URI = "https://repo.or.cz/syslinux.git/snapshot/458a54133ecdf1685c02294d812cb562fe7bf4c3.tar.gz"

SRC_URI[md5sum] = "4d24130977cd08ae486bee69613c1f25"
SRC_URI[sha256sum] = "013661efc770a7e12206acb14edf3c670b2b514352e9a4bae1039102104c1445"

COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'
# Don't let the sanity checker trip on the 32 bit real mode BIOS binaries
INSANE_SKIP_${PN}-misc = "arch"
INSANE_SKIP_${PN}-chain = "arch"

EXTRA_OEMAKE = " \
	BINDIR=${bindir} SBINDIR=${sbindir} LIBDIR=${libdir} \
	DATADIR=${datadir} MANDIR=${mandir} INCDIR=${includedir} \
"

do_configure() {
	# drop win32 targets or build fails
	# sed -e 's,win32/\S*,,g' -i Makefile

	# clean installer executables included in source tarball
	oe_runmake spotless
	oe_runmake clean
	# NOTE: There is a temporary work around above to specify
	#	the efi32 as the firmware else the pre-built bios
	#	files get erased contrary to the doc/distib.txt
	#	In the future this should be "bios" and not "efi32".
}

do_compile() {
	# Make sure the recompile is OK.
	# Though the ${B} should always exist, still check it before find and rm.
	[ -d "${B}" ] && find ${B} -name '.*.d' -type f -exec rm -f {} \;

	unset LDFLAGS
	# Rebuild only the installer; keep precompiled bootloaders
	# as per author's request (doc/distrib.txt)
	oe_runmake bios
	oe_runmake installer
}

do_install() {
	oe_runmake CC="${CC} ${CFLAGS}" LD="${LD}" install INSTALLROOT="${D}" firmware="bios"

	install -d ${D}${datadir}/syslinux/
	install -m 644 ${S}/bios/core/ldlinux.sys ${D}${datadir}/syslinux/
	install -m 644 ${S}/bios/core/ldlinux.bss ${D}${datadir}/syslinux/
	install -m 755 ${S}/bios/linux/syslinux-nomtools ${D}${bindir}/
}

PACKAGES += "${PN}-nomtools ${PN}-extlinux ${PN}-mbr ${PN}-chain ${PN}-pxelinux ${PN}-isolinux ${PN}-misc"

RDEPENDS_${PN} += "mtools"
RDEPENDS_${PN}-nomtools += "libext2fs"
RDEPENDS_${PN}-misc += "perl"

FILES_${PN} = "${bindir}/syslinux"
FILES_${PN}-nomtools = "${bindir}/syslinux-nomtools"
FILES_${PN}-extlinux = "${sbindir}/extlinux"
FILES_${PN}-mbr = "${datadir}/${BPN}/mbr.bin"
FILES_${PN}-chain = "${datadir}/${BPN}/chain.c32"
FILES_${PN}-isolinux = "${datadir}/${BPN}/isolinux.bin"
FILES_${PN}-pxelinux = "${datadir}/${BPN}/pxelinux.0"
FILES_${PN}-dev += "${datadir}/${BPN}/com32/lib*${SOLIBS} ${datadir}/${BPN}/com32/include ${datadir}/${BPN}/com32/com32.ld"
FILES_${PN}-staticdev += "${datadir}/${BPN}/com32/lib*.a ${libdir}/${BPN}/com32/lib*.a"
FILES_${PN}-misc = "${datadir}/${BPN}/* ${libdir}/${BPN}/* ${bindir}/*"

BBCLASSEXTEND = "native nativesdk"
