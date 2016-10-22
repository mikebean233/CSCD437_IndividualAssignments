#include<stdlib.h>
#include<string.h>
#include<stdio.h>

#define BUFFER_SIZE 64

// This function was blindly copying bytes from arg into out without verifying the size of the output array
//  foo(char *arg, char *out          )
int foo(char *arg, char *out, size_t n)
{
// Use strncpy instead of strcpy so that the number of bytes can be specified
//  strcpy (out, arg   );
    strncpy(out, arg, n);
    return 0;
}

int main(int argc, char *argv[])
{
    char buf[BUFFER_SIZE];

    // It's good practice to initialize our buffer
    int i = 0;
    for(; i < BUFFER_SIZE; ++i){
        buf[i] = '\0';
    }


    if (argc != 2)
    {
        fprintf(stderr, "a: argc != 2\n");
        exit(EXIT_FAILURE);
    }

//  foo(argv[1], buf             );
    foo(argv[1], buf, BUFFER_SIZE);
    return 0;
}

/****************************

 Problems Found:
 in foo():
    strcpy(dst, src) will blindly copy bytes from source to destination until it runs into a '\0' character in the source,
    this is a vulnerability becuase the source string can be longer the destination buffer.

 Fixes:
    Add a length parameter to foo() so that the destinatio buffer size can be specified,
    then use strncpy(dst, src, n) to perform the copying.


 Original Implementation


int foo(char *arg, char *out)
{
    strcpy(out, arg);
    return 0;
}

int main(int argc, char *argv[])
{
    char buf[64];
    if (argc != 2)
    {
        fprintf(stderr, "a: argc != 2\n");
        exit(EXIT_FAILURE);
    }

    foo(argv[1], buf);
    return 0;
}

 **/