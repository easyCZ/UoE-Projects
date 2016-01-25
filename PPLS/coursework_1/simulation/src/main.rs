
#[derive(Debug)]
struct ThreadOne {
    count: usize,
    can_execute: bool
}

impl ThreadOne {
    fn new() -> ThreadOne {
        ThreadOne {
            count: 0,
            can_execute: true
        }
    }

    fn execute(&self, x: usize, y: usize) -> (usize, usize) {
        while x != y {
            *x = *x - 1;
        }
        *y = *y + 1;

        (x, y)
    }


}


fn main() {

    let mut x = 10;
    let mut y = 0;

    let thread_one = ThreadOne::new();

    println!("thread one: {:?}", thread_one);

    println!("Hello, world!");
}
