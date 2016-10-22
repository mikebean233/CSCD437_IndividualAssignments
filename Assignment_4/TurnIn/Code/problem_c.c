#include<stdlib.h>
#include<string.h>
#include<stdio.h>

#define BUFFER_SIZE  128

int bar(char *arg, char *targ, int ltarg)
{
//  int len, i;
    int copyCharCount = 0, i = 0;

//  The previous implementation used incorrect magic numbers to limit the number of characters that are copied into the buffer
//  len = strlen(arg);
//  if (len > ltarg)
//      len = ltarg;

//  These two lines are the equivalent to max(BUFFER_SIZE, strlen(argv[1])), but don't necessarily iterate through the
//  entire array as strlen() would
    while(copyCharCount < ltarg && arg[copyCharCount] != '\0')
        ++copyCharCount;

//  Since our buffer has been initialized with null characters, we don't have to copy over the '\0' at the end of
//  targ (using < instead of <=))
//    for (i = 0; i <= len; i++)
//        targ[i] = arg[i];

    for (i = 0; i < copyCharCount; i++)
        targ[i] = arg[i];

    return 0;
}

int foo(char *arg)
{
    int i = 0;

//  char buf[128];
    char buf[BUFFER_SIZE];

    // It's good practice to initialize our buffer
    for(i = 0; i < BUFFER_SIZE; ++i)
        buf[i] = '\0';
    i = 0;



//  bar(arg, buf, 140);
    bar(arg, buf, BUFFER_SIZE);
    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "c: argc != 2\n");
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

 in bar():
    2) Using strlen() to determine a user provided strings length can be a vulnerability becuase the string length could exceed the maximum value
       for the type returned by strlen() and cuase a rollover resulting in a negative value being returned.

 in main():
    3) The buffer should be initialized with null characters

 Fixes:
    1) Fix the magic numbers (I did this by declaring a symbol BUFFER_SIZE with a #define directive and using it instead of int literals)
    2) I wrote a loop that will determine the length of the input string if it's shorter then the buffer size, the loop is not vulnerable
       to the overflow problem of strlen becuase it will only iterate as many times as there are elemnts in the buffer.
    3) Initialize the buffer using a loop.





Original Implementation

int bar(char *arg, char *targ, int ltarg)
{
    int len, i;
    len = strlen(arg);
    if (len > ltarg)
        len = ltarg;

    for (i = 0; i <= len; i++)
        targ[i] = arg[i];

    return 0;
}

int foo(char *arg)
{
    char buf[128];
    bar(arg, buf, 140);
    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "c: argc != 2\n");
        exit(EXIT_FAILURE);
    }

    foo(argv[1]);
    return 0;
}
 **/