#include<stdlib.h>
#include<string.h>
#include<stdio.h>

#define BUFFER_SIZE  128

int foo(char *arg)
{
//  Initialize Variables
//  int len, i;
    int copyCharCount = 0, i  = 0;

//  It's more convenient to use the pre processor to manage magic numbers
//  char buf[128];
    char buf[BUFFER_SIZE];

    // It's good practice to initialize our buffer
    for(i = 0; i < BUFFER_SIZE; ++i)
        buf[i] = '\0';
    i = 0;

//  The previous implementation used incorrect magic numbers to limit the number of characters that are copied into the buffer
//  len = strlen(arg);
//  if (len > 136)
//      len = 136;

//  These two lines are the equivalent to min(BUFFER_SIZE, strlen(argv[1])), but don't necessarily iterate through the
//  entire array as strlen() would
    while(copyCharCount < BUFFER_SIZE && arg[copyCharCount] != '\0')
        ++copyCharCount;

//  Since our buffer has been initialized with null characters, we don't have to copy over the '\0' at the end of
//  arg (using < instead of <=))
//  for (i = 0; i <= len; i++)
//      buf[i] = arg[i];
    for(i = 0; i < copyCharCount; ++i)
        buf[i] = arg[i];

    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "b: argc != 2\n");
        exit(EXIT_FAILURE);
    }

    foo(argv[1]);

    return 0;
}


/****************************

 Problems Found:
 in foo():
    1) The magic number used to determine if the input string are incorrect (it is different then the one used to declare the buffer),
       this could cause too many bytes to be copied over to the output array.
    2) The loop copying the bytes into the buffer is going one char to far becuase of the condition being used is inclusive, this could write over
       what should be a null char in the buff with one to many chars from the input array.
    3) The buffer is not initilaized with null characters.


 Fixes:
    1) Fix the magic numbers (I did this by declaring a symbol BUFFER_SIZE with a #define directive and using it instead of int literals)
    2) Change the condition to < instead of <=
    3) Initialize the buffer right after it is declared.



Original Implementation


int foo(char *arg)
{
    char buf[128];
    int len, i;

    len = strlen(arg);
    if (len > 136)
        len = 136;

    for (i = 0; i <= len; i++)
        buf[i] = arg[i];

    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "b: argc != 2\n");
        exit(EXIT_FAILURE);
    }

    foo(argv[1]);
    return 0;
}
 **/