DESCRIPTION = "Overwrite inittab file from base files and use custom inittab instead"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://inittab"

S = "${WORKDIR}"

do_install_append() {
	
	install -d -m 755 ${D}/etc
	install -m 0755 ${S}/inittab ${D}/etc/
}
