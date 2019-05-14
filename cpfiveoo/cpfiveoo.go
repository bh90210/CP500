package cpfiveoo

import (
	"bytes"
	"encoding/base64"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"net/url"
	"os"
	"path/filepath"
	"strconv"
	"strings"

	"github.com/ChimeraCoder/anaconda"
	"github.com/dgraph-io/badger"
	"gopkg.in/ahmdrz/goinsta.v2"
)

var totalIdz = make(map[int]string)
var db *badger.DB
var dbOpenHelper string

// InitDBdirHelper open the database
func InitDBdirHelper(dir string) {
	dbOpenHelper = dir
}

func openDB() {
	opts := badger.DefaultOptions // optimize for smartphones
	opts.Dir = dbOpenHelper
	opts.ValueDir = dbOpenHelper
	badg, _ := badger.Open(opts)
	db = badg
}

func bytesToString(data []byte) string {
	//fmt.Println("converted bytes to string")
	return string(data[:])
}

// DbView database lookup, takes a string (Key)
func DbView(dbEntry string) string {
	openDB()
	defer db.Close()

	var varDBentry []byte
	// badger view entry function
	db.View(func(txn *badger.Txn) error {
		item, _ := txn.Get([]byte(dbEntry))
		item.Value(func(val []byte) error {
			varDBentry = append([]byte{}, val...)
			return nil
		})
		return nil
	})
	return bytesToString(varDBentry)
}

// DbUpdate writes to the database, takes string (key), string (value)
func DbUpdate(dbEntry, value string) {
	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		err := txn.Set([]byte(dbEntry), []byte(value))
		return err
	})
}

// TotalScheduledPosts calculate total scheduled post nuber and return (int) result
// to menu inflater for-loop
func TotalScheduledPosts() int {
	openDB()
	defer db.Close()

	var totalPostsNumber int

	db.View(func(txn *badger.Txn) error { // abstract function for database query
		opts := badger.DefaultIteratorOptions
		opts.PrefetchValues = false
		it := txn.NewIterator(opts)
		defer it.Close()
		prefix := []byte("POST")
		for it.Seek(prefix); it.ValidForPrefix(prefix); it.Next() {
			item := it.Item()
			k := item.Key()
			id := strings.Split(bytesToString(k), "_")[1]
			totalIdz[totalPostsNumber] = id
			totalPostsNumber++
		}
		return nil
	})

	return totalPostsNumber
}

// ScheduledIds helper fulction to pass post's id to inflated menu, take an int (coming from inflater for-loop),
// returns a string (post's id)
func ScheduledIds(i int) string { // abstract function for database query
	id := totalIdz[i]
	return id
}

//ScheduledPostUpload jobBuilder's backend function
func ScheduledPostUpload(receivedID string) {
	text := DbView(receivedID + "_TEXT")
	twHash := DbView(receivedID + "_TWHASH")
	inHash := DbView(receivedID + "_INHASH")
	filePath := DbView(receivedID + "_FILEPATH")

	value := DbView(receivedID + "_FB")
	if value == "true" {
		facebookPhotoPost(text, filePath)
	}

	value = DbView(receivedID + "_TW")
	if value == "true" {
		twitterPhotoPost(text, twHash, filePath)
	}

	value = DbView(receivedID + "_IN")
	if value == "true" {
		instagramPhotoPost(text, inHash, filePath)
	}

	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		txn.Delete([]byte("POST_" + receivedID + "_ID"))
		txn.Delete([]byte(receivedID + "_TEXT"))
		txn.Delete([]byte(receivedID + "_TWHASH"))
		txn.Delete([]byte(receivedID + "_INHASH"))
		txn.Delete([]byte(receivedID + "_FILEPATH"))
		txn.Delete([]byte(receivedID + "_FB"))
		txn.Delete([]byte(receivedID + "_TW"))
		txn.Delete([]byte(receivedID + "_IN"))
		return nil
	})
}

// ScheduledPostNow Package sort provides primitives for sorting slices and user-defined
// collections.
//ScheduledPostNowPartTwo helper function for post not button on scrolling_activity
func ScheduledPostNow(receivedID string) {
	text := DbView(receivedID + "_TEXT")
	twHash := DbView(receivedID + "_TWHASH")
	inHash := DbView(receivedID + "_INHASH")
	filePath := DbView(receivedID + "_FILEPATH")

	value := DbView(receivedID + "_FB")
	if value == "true" {
		facebookPhotoPost(text, filePath)
	}

	value = DbView(receivedID + "_TW")
	if value == "true" {
		twitterPhotoPost(text, twHash, filePath)
	}

	value = DbView(receivedID + "_IN")
	if value == "true" {
		instagramPhotoPost(text, inHash, filePath)
	}

	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		txn.Delete([]byte("POST_" + receivedID + "_ID"))
		txn.Delete([]byte(receivedID + "_TEXT"))
		txn.Delete([]byte(receivedID + "_TWHASH"))
		txn.Delete([]byte(receivedID + "_INHASH"))
		txn.Delete([]byte(receivedID + "_FILEPATH"))
		txn.Delete([]byte(receivedID + "_FB"))
		txn.Delete([]byte(receivedID + "_TW"))
		txn.Delete([]byte(receivedID + "_IN"))
		return nil
	})
}

// DeletePost Package sort provides primitives for sorting slices and user-defined
// collections.
func DeletePost(receivedID string) {
	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		txn.Delete([]byte("POST_" + receivedID + "_ID"))
		txn.Delete([]byte(receivedID + "_TEXT"))
		txn.Delete([]byte(receivedID + "_TWHASH"))
		txn.Delete([]byte(receivedID + "_INHASH"))
		txn.Delete([]byte(receivedID + "_FILEPATH"))
		txn.Delete([]byte(receivedID + "_FB"))
		txn.Delete([]byte(receivedID + "_TW"))
		txn.Delete([]byte(receivedID + "_IN"))
		return nil
	})
}

// DelHelper Package sort provides primitives for sorting slices and user-defined
// collections.
func DelHelper(receivedID string) {
	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		txn.Delete([]byte(receivedID))
		return nil
	})
}

// Schedule function to store data of post to DB
func Schedule(text, twHash, inHash, filePath string) string {
	var postID uint64
	fbDBentry := DbView("fbCheckedMem")
	twDBentry := DbView("twCheckedMem")
	inDBentry := DbView("inCheckedMem")

	openDB()
	defer db.Close()

	db.Update(func(txn *badger.Txn) error {
		seq, _ := db.GetSequence([]byte("IDZ"), 1000)
		defer seq.Release()
		id, _ := seq.Next()
		err := txn.Set([]byte("POST_"+string(id)+"_ID"), []byte(string(id)))

		txn.Set([]byte(string(id)+"_TEXT"), []byte(text))
		txn.Set([]byte(string(id)+"_TWHASH"), []byte(twHash))
		txn.Set([]byte(string(id)+"_INHASH"), []byte(inHash))
		txn.Set([]byte(string(id)+"_FILEPATH"), []byte(filePath))
		//err = txn.Set([]byte("POST_"+string(id)+"_TIME"), []byte("test"))

		txn.Set([]byte(string(id)+"_FB"), []byte(fbDBentry))
		txn.Set([]byte(string(id)+"_TW"), []byte(twDBentry))
		txn.Set([]byte(string(id)+"_IN"), []byte(inDBentry))

		postID = id
		return err
	})
	return string(postID)
}

// PostNow Package sort provides primitives for sorting slices and user-defined
// collections.
func PostNow(text string, twHash string, inHash string, filePath string) {
	value := DbView("fbCheckedMem")
	if value == "true" {
		facebookPhotoPost(text, filePath)
	}

	value = DbView("twCheckedMem")
	if value == "true" {
		twitterPhotoPost(text, twHash, filePath)
	}

	value = DbView("inCheckedMem")
	if value == "true" {
		instagramPhotoPost(text, inHash, filePath)
	}
}

func facebookPhotoPost(text string, photoFilePath string) {
	fbID := DbView("fbID")
	fbTOKEN := DbView("fbTOKEN")
	// FACEBOOK
	urlll := "https://graph.facebook.com"
	resource := "/v3.2/" + fbID + "/photos"
	u, _ := url.ParseRequestURI(urlll)
	u.Path = resource
	urlStr := u.String() // "https://api.com/user/"
	extraParams := map[string]string{
		"message": text,
	}
	file, _ := os.Open(photoFilePath)
	defer file.Close()
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, _ := writer.CreateFormFile("source", filepath.Base(photoFilePath))
	io.Copy(part, file)
	for key, val := range extraParams {
		_ = writer.WriteField(key, val)
	}
	writer.Close()
	// Create a new request using http
	req, _ := http.NewRequest("POST", urlStr, body)
	// Create a Bearer string by appending string access token
	bearer := "Bearer " + fbTOKEN // when
	// add authorization header to the req
	req.Header.Set("Content-Type", writer.FormDataContentType())
	req.Header.Add("Authorization", bearer)
	// Send req using http Client
	client := &http.Client{}
	client.Do(req)
	//bodyy, _ := ioutil.ReadAll(res.Body)
}

func twitterPhotoPost(text string, hashes string, twFilePath string) {
	twAPI := DbView("twAPI")
	twAPIsec := DbView("twAPIsec")
	twTOKEN := DbView("twTOKEN")
	twTOKENsec := DbView("twTOKENsec")
	anaconda.SetConsumerKey(twAPI)
	anaconda.SetConsumerSecret(twAPIsec)
	api := anaconda.NewTwitterApi(twTOKEN, twTOKENsec)
	dataa, _ := ioutil.ReadFile(twFilePath)
	mediaResponse, _ := api.UploadMedia(base64.StdEncoding.EncodeToString(dataa))
	v := url.Values{}
	v.Set("media_ids", strconv.FormatInt(mediaResponse.MediaID, 10))
	api.PostTweet(text+hashes, v)
}

func instagramPhotoPost(text string, hashes string, inFilePath string) {
	inUSER := DbView("inUSER")
	inPASS := DbView("inPASS")
	insta := goinsta.New(inUSER, inPASS)
	insta.Login()
	dataa, _ := ioutil.ReadFile(inFilePath)
	r := bytes.NewReader(dataa)              // ola ta lefta
	insta.UploadPhoto(r, text+hashes, 87, 0) // default quality is 87
}
