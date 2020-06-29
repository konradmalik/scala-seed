PROJECT=scala-seed
OWNER=konradmalik
SCALA_BIN_VER=2.12

define build_module
	docker build \
	--build-arg owner=${OWNER} \
	--build-arg project=${PROJECT} \
	--build-arg scala_binary_ver=${SCALA_BIN_VER} \
	--build-arg module_name=$(1) \
	-t ${OWNER}/$(1) $(1)
endef

build-builder:
	docker build --compress -t ${OWNER}/${PROJECT}-builder .

build-spark: build-builder
	$(call build_module,spark)

build-all: build-spark
	echo "all done!"
