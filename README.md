# CS-Preview

Generate a snazzy code block image from a colorscheme.

# Example

Input (same as `test-input`):
```
#F5E9DA #232136 #B4637A #569F84 #EA9D34 #286983 #907AA9 #56959F #F2E9DE #F2E9DE
  
33333 1111111133333 11111111
    3333 11
    33333 111 11
    77777 33333 11111111111 3333
    
    111 111111 22222
    111 1111111
    111 111 3333 1111111
    111 2222222222 11111111 2222
    111 111 33333 1111111
    111 111 11111111111 1111111111111111111
    
    666666 11
1
```

Output:
![Example Output](https://raw.githubusercontent.com/L3afMe/cs-preview/master/test-output.jpg)

## Download prebuilt jars

Download from https://github.com/l3afme/cs-preview/releases

## Build

Run the project directly:

    $ clojure -M:run-m <input-file>

Compile to jar:

    $ clojure -T:build ci

Run that jar:

    $ java -jar target/cs-preview-0.1.0-SNAPSHOT.jar

