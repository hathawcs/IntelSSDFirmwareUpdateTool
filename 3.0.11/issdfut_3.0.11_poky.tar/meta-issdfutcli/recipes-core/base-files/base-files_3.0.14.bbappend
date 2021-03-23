DESCRIPTION = "Overwrite profile file from base files and use custom profile instead"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://profile"

S = "${WORKDIR}"

do_install_append() {
	
	install -d -m 755 ${D}/etc
	install -m 0755 ${S}/profile ${D}/etc/
}
