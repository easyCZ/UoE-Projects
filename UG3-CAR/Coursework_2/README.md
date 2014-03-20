####   Computer Architecture 2014 - University of Edinburgh
####   Student ID: s1115104

Both types of cache simulators are implemented.


To run the Direct mapped cache simulator, execute the following:

  python2.7 cache.py direct_mapped file_name cache_size
  
  
To run the Set Associative cache simulator, execute the following:
  
  python2.7 cache.py set_associative file_name cache_size -set_size set_size

-set_size is an optional flag, default set_size is 1

file_name - The file to be simulated on
cache_size - the size of the cache in KB
set_size - The associativity of the cache - the number of elements in each set.
