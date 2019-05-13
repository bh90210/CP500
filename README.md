# CP500
Social media automation app for Android

There are two ways acquiring the app. You can either **A** download a pre-built APK or **B** build from source localy.  
NOTICE: for option **B**2 to work you will need Go. For platform specific instructions please see [the official documentation](https://golang.org/doc/install) 

* **A** You can download the latest release here
  
* **B** There are two options to procced depending on your situation

1. Android Studio/Gradle option: run `git clone https://github.com/BH90210/CP500.git` from inside the directory you want your project to be. Then procced normally as you would since the binding is already pre-compiled and in place

2. Go option: run `go get -u github.com/BH90210/CP500` then procced building the app the way you prefer

## How to compile the Go binding

> For this to work you will also need Gomobile  
> To get it run `go get golang.org/x/mobile/cmd/gomobile`  
> and then `gomobile init`  
> for more detailed instructions please see [here](https://godoc.org/golang.org/x/mobile/cmd/gomobile) and [here](https://github.com/golang/go/wiki/Mobile)

Simply run `gomobile bind -o $GOPATH/src/github.com/BH90210/CP500/AndroidStudioProject/app/cpfiveoo.aar -target=android github.com/BH90210/CP500/cpfiveoo` and you should end up with two files (`cpfiveoo.aar` & `cpfiveoo.jar`) inside your `/app/` dir. Afterwards you have to refresh Gradle. You can safely delete the `.jar` file

TODO
- [ ] Error handling
- [ ] Multiple photos/videos upload
- [ ] Multiple accounts support
- [ ] Stories
- [ ] Cross platform text only posts 
- [ ] Pintrest, Tumblr, Imgur support
- [ ] Schedule further than 24H window
