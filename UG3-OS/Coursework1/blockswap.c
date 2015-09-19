
/* looper.c -- start a kernel thread that calls a function
   every second or so */


/* Declare what kind of code we want from the header files */
#define __KERNEL__         /* We're part of the kernel */
#define MODULE             /* Not a permanent part, though. */

#include <linux/kernel.h>
/* Standard headers for LKMs */
#include <linux/modversions.h> 
#include <linux/module.h>  

/* include swap.h for the memory stuff */
#include <linux/slab.h>
#include <linux/swap.h>
#include <linux/swapctl.h>
#include <linux/pagemap.h>

/* routine always returns 1, blocks all swapping */
static int blockswap(struct mm_struct *mm,unsigned int priority, unsigned int gfp_mask, zone_t * classzone) {
  return 1;
}

/* Initialize the module - insert our hook into page swapout */
int init_module()
{
  dont_swap_mm = blockswap;
  return 0;
}


/* Cleanup */
void cleanup_module()
{
  dont_swap_mm = NULL;
  return;
}
