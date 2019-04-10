package nz.co.cocca.test.javatest;

import org.springframework.http.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller 
{

	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public String error_message;
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	//@RequestMapping("/users")
    @RequestMapping(value="/users", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAllUsers() {
    	
    	// Requirement:
    	
    	// Retrieve all users from https://reqres.in/api/users
    	// Note that https://reqres.in/api/users returns a pageable result. You'll need to handle the pagination logic and to dump all users from that API.  
    	// After you dumped all users from the API, sort the records in alphabetical order based on each person's first_name.
    	
    	// Implementation starts below
    	
    	usersdoc element = new usersdoc();
    	if (!element.execute()) { return new ResponseEntity<JSONObject>(element.as_error(), HttpStatus.OK); }
    	if (!element.sort("first_name")) { return new ResponseEntity<JSONObject>(element.as_error(), HttpStatus.OK); }
    	return new ResponseEntity<JSONArray>(element.as_object(), HttpStatus.OK);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/
}