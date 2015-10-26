ifneq ($(KERNELRELEASE),)
# kbuild part of makefile
obj-m := worker1.o

else
# Normal Makefile
KERNELDIR := /lib/modules/2.6.21.1/build

all::
	$(MAKE) -C $(KERNELDIR) M=`pwd` modules

endif
