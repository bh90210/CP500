# CP500
Social media automation app for Android

Build Go from source

Assuming everything is in default location and $GOPATH set run this in your terminal
`gomobile bind -o $GOPATH/src/github.com/BH90210/CP500/AndroidStudioProject/app/cpfiveoo.aar -target=android github.com/BH90210/CP500/cpfiveoo` and you should end up with two files (cpfiveoo.aar & cpfiveoo.jar) inside /app/ dir. You can safely delete the .jar file.

TODO
- [ ] Error handling
- [ ] Multiple photos/videos upload
- [ ] Multiple accounts support
- [ ] Stories
- [ ] Cross platform text only posts 
- [ ] Pintrest, Tumblr, Imgur support
- [ ] Schedule further than 24H window
