/* module.c
 *
 * Replaces the default memory swapper swap_out ()
 * after it is loaded.
 *
 * The module should set the swapper function pointer with it's 
 * swap function address before executing any code.
 *
 * Written by: Gagarine Yaikhom
 */

/* use some special CS3 stuff */
#define __CS3_UML__
/* Declare what kind of code we want from the header files */
#define __KERNEL__         /* We're part of the kernel */
#define MODULE             /* Not a permanent part, though. */

#include <linux/module.h>  /* Needed by all modules */
#include <linux/kernel.h>  /* Needed for KERN_ALERT */

/* From vmscan.c include: START */
#include <linux/slab.h>
#include <linux/kernel_stat.h>
#include <linux/swap.h>    
#include <linux/swapctl.h>
#include <linux/smp_lock.h>
#include <linux/pagemap.h>
#include <linux/init.h>
#include <linux/highmem.h>
#include <linux/file.h>

#include <asm/pgalloc.h>
/* From vmscan.c include: END */
#include <linux/cs3_uml.h>

extern cs3_swap_func_t cs3_swap_func;
extern struct mm_struct *swap_mm;

int swap_out (unsigned int priority, unsigned int gfp_mask, zone_t * classzone);
int swap_out_mm (struct mm_struct *, int, int *, zone_t *);

int init_module(void)
{
   static int FASTCALL(user_swap_func(unsigned int, unsigned int, zone_t *));

   printk("<1>Setting function pointer to 'user_swap_func ()'\n");
   cs3_swap_func = user_swap_func;
   printk("<1>Setting function pointer: DONE\n");
	
   // A non 0 return means init_module failed; module can't be loaded.
   return 0;
}

static int user_swap_func(unsigned int priority, unsigned int gfp_mask, zone_t * classzone)
{
	int counter, nr_pages = SWAP_CLUSTER_MAX;
	struct mm_struct *mm;

	// printk("<1> Using user swapper 'user_swap_func ()'\n");
	counter = mmlist_nr;
	do {
		if (unlikely(current->need_resched)) {
			__set_current_state(TASK_RUNNING);
			schedule();
		}

		spin_lock(&mmlist_lock);
		mm = swap_mm;
		while (mm->swap_address == TASK_SIZE || mm == &init_mm) {
			mm->swap_address = 0;
			mm = list_entry(mm->mmlist.next, struct mm_struct, mmlist);
			if (mm == swap_mm)
				goto empty;
			swap_mm = mm;
		}

		/* Make sure the mm doesn't disappear when we drop the lock.. */
		atomic_inc(&mm->mm_users);
		spin_unlock(&mmlist_lock);

		nr_pages = swap_out_mm(mm, nr_pages, &counter, classzone);

		mmput(mm);

		if (!nr_pages)
			return 1;
	} while (--counter >= 0);

	return 0;

empty:
	spin_unlock(&mmlist_lock);
	return 0;
}

void cleanup_module(void)
{
   printk("<1>Resetting function pointer to 'swap_out ()'\n");
   cs3_swap_func = swap_out;
   printk("<1>Resetting function pointer: DONE\n");
}  
