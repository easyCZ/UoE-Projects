
/* looper.c -- start a kernel thread that calls a function
   every second or so */


/* Standard headers for LKMs */
#include <linux/kernel.h>
#include <linux/module.h>  

/* We also need the ability to put ourselves to sleep 
 * and wake up later */
#include <linux/sched.h>

#include <linux/delay.h>

/* This is used by cleanup, to prevent the module from 
 * being unloaded while the kernel thread is still active */
static DECLARE_WAIT_QUEUE_HEAD(WaitQ);

static int worker_routine(void *);
static int please_clock_off = 0; /* signal to thread */


/* This is the routine that is run by the kernel thread we
   start when the modulue is loaded */
static int worker_routine(void *irrelevant)
{
  /* drop userspace stuff */
  daemonize("worker");
  printk("Your worker is clocking on\n");
  /* now do the work */
  while ( 1 ) {
    //    mdelay(1000);
    /* If cleanup wants us to die */
    if (please_clock_off) {
      printk("Your worker is clocking off\n");
      wake_up(&WaitQ);   /* Now cleanup_module can return */
      complete_and_exit(NULL,0);  /* terminate the kernel thread */
    } else {
      /* do some work */
      printk("Hello, this is your worker working hard\n");
      /* schedule timeout will busy wait unless we say otherwise */
      /* sleep for 10 seconds */
      set_current_state(TASK_INTERRUPTIBLE);
      schedule_timeout(10*HZ);
    }
  }
}

/* Initialize the module - start kernel thread */
int init_module()
{
  kernel_thread(worker_routine,NULL,0);
  return 0;
}


/* Cleanup */
void cleanup_module()
{
  please_clock_off = 1;
  /* Wait for the worker to notice that we're waiting, and exit */
  sleep_on(&WaitQ);
}
