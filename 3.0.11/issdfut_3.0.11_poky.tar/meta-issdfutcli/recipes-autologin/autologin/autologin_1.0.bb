
LICENSE = "Intel-binary-only"
LICENSE_FLAGS = "license_${PN}_${PV}"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-issdfutcli/licenses/Intel-binary-only;md5=7735bfc28b7024d9c47f3ea6d35fb434"


ASNEEDED = ""

PR = "r1"

DEPENDS = "boost"

SRC_URI = "file://autologin_src.tar.gz"

S = "${WORKDIR}/autologin_src"

INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

EXTRA_OEMAKE = "'GPP=${CXX}'"

PARALLEL_MAKE = ""

do_install() {
	install -d -m 755 ${D}/usr/sbin
	install -p -m 755 ${S}/autologin ${D}/usr/sbin/
}


