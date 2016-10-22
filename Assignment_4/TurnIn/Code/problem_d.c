#include<stdlib.h>
#include<string.h>
#include<stdio.h>

#define BUFFER_SIZE 1024

//int foo(char *arg, short arglen)
int foo(char *arg                )
{
//  char buf[1024];
    char buf[BUFFER_SIZE];
//  int i,     maxlen = 1024;
    int i = 0, maxlen = BUFFER_SIZE, charCopyCount = 0;

    for(i = 0; i < BUFFER_SIZE; ++i)
        buf[i] = '\0';


//  if (arglen < maxlen)
//  {
//      for (i = 0; i < strlen(arg); i++)
//          buf[i] = arg[i];
//  }

    while(charCopyCount < maxlen && arg[charCopyCount] != '\0')
        ++charCopyCount;

    for(i = 0; i < charCopyCount; ++i)
        buf[i] = arg[i];

    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "d: argc != 2\n");
        exit(EXIT_FAILURE);
    }

//  foo(argv[1], strlen(argv[1]));
    foo(argv[1]                 );
    return 0;
}




/****************************

 Problems Found:
 in foo():
    1) Using strlen in the for loop is not only very inefficient but insecure becuse of the return value can end up being
       negative if the number of bytes in arg is greater then the maximum value of strlen()'s return type.
    2) The buffer should be initialized;
 in main():
    3) Again, strlen() shouldn't be used get the length of arg[1]



 Fixes:
    1) find min( length(buf), length(arg)) using a while loop and copy that many characters into the buffer
    2) Initialize the buffer
    3) remove the length parameter from foo()


int foo(char *arg, short arglen)
{
    char buf[1024];
    int i, maxlen = 1024;

    if (arglen < maxlen)
    {
        for (i = 0; i < strlen(arg); i++)
            buf[i] = arg[i];
    }

    return 0;
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "d: argc != 2\n");
        exit(EXIT_FAILURE);
    }

    foo(argv[1], strlen(argv[1]));
    return 0;
}
 **/