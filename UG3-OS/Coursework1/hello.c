/* hello.c 
 * 
 * "Hello, world" - the loadable kernel module version. 
 *
 */

/* Standard headers for LKMs */
#include <linux/module.h>  
#include <linux/kernel.h>

#include <linux/tty.h>      /* console_print() interface */

/* Initialize the LKM */
int init_module()
{
  console_print("Hello, world - this is the kernel speaking\n");
  /* More normal is printk(), but there's less that can go wrong with 
     console_print(), so let's start simple.
  */

  /* If we return a non zero value, it means that 
   * init_module failed and the LKM can't be loaded 
   */
  return 0;
}


/* Cleanup - undo whatever init_module did */
void cleanup_module()
{
  console_print("Short is the life of an LKM\n");
}
