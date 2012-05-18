# About

Prototype for LESS file rendering with OcpSoft Rewrite.

# Usage

Add the LESS renderer to your Rewrite configuration:

    .addRule(Less.fileType("less"))

Now create a LESS file with the file extension `less` in your application. For example `/styles/default.less`

    @nice-blue: #5B83AD;
    @light-blue: @nice-blue + #111;
    
    #header { 
      color: @light-blue; 
    }

When you access this file with your browser you will get:

    #header { 
      color: #6c94be; 
    }