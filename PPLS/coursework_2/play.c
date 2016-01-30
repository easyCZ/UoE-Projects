#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
#include <unistd.h>

#define send_data_tag 2001
#define return_data_tag 2002
#define SLEEPTIME 1000000

int numbers_size = 10000;

int main(int argc, char **argv) {

	int ierr;
	int my_id, num_procs;
	MPI_Status status;

	int root_process = 0;

	ierr = MPI_Init(&argc, &argv);

	ierr = MPI_Comm_rank(MPI_COMM_WORLD, &my_id);
	ierr = MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

	if (my_id == root_process) {
		int chunk_size = numbers_size / num_procs;
		int process_id;
		int lower_bound = 0;
		int upper_bound = chunk_size;


		for (process_id = 0; process_id < num_procs; process_id++) {
			printf("Chunk %i, bounds: %i - %i\n", process_id, lower_bound, upper_bound);

			// send lower bound
			ierr = MPI_Send(&lower_bound, 1 , MPI_INT, process_id, send_data_tag, MPI_COMM_WORLD);
			// send upper bound
			ierr = MPI_Send(&upper_bound, 1 , MPI_INT, process_id, send_data_tag, MPI_COMM_WORLD);

			lower_bound += chunk_size;
			upper_bound += chunk_size;
		}

		// Collect replies
		int total_sum = 0;
		for(process_id = 1; process_id < num_procs; process_id++) {
			int partial_sum = 0;
			int sender;

            ierr = MPI_Recv(&partial_sum, 1, MPI_LONG, MPI_ANY_SOURCE, return_data_tag, MPI_COMM_WORLD, &status);

            sender = status.MPI_SOURCE;
            printf("Partial sum %i returned from process %i\n", partial_sum, sender);

            total_sum += partial_sum;
        }

        printf("Grand total: %i\n", total_sum);

	}

	// Slave
	else {
		int lower_bound, upper_bound;
		ierr = MPI_Recv( &lower_bound, 1, MPI_INT, root_process, send_data_tag, MPI_COMM_WORLD, &status);
		ierr = MPI_Recv( &upper_bound, 1, MPI_INT, root_process, send_data_tag, MPI_COMM_WORLD, &status);

		printf("Proc #%i received: %i - %i\n", my_id, lower_bound, upper_bound);

		int i;
		int sum = 0;
		for (i = lower_bound; i < upper_bound; i++) {
			sum += lower_bound + i;
		}

		usleep(SLEEPTIME);
		ierr = MPI_Send(&sum, 1, MPI_INT, root_process, return_data_tag, MPI_COMM_WORLD);
	}

	ierr = MPI_Finalize();


	// int ierr, num_procs, my_id;
	// int numbers[numbers_size];

	// /* initialize */
	// int i;
	// for (i = 0; i < numbers_size; i++) {
	// 	numbers[i] = i;
	// }

	// int sum = 0;
	// for (i = 0; i < numbers_size; i++) {
	// 	sum += numbers[i];
	// }

	// printf("The grand total is: %i\n", sum);
}
