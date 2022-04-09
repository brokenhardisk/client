import React from 'react';
class AddRecord extends React.Component {
  constructor(props) {
    super(props);
	this.state = {
       isLoaded: false,
       items: [],
	   emailForPost: "",
	   paramsForPostValid: false,
	   startForPost: "",
	   endForPost: "",
	   errorForEmail: "",
	   errorForStart: "",
	   errorForEnd: "",
	   error: "",
	   emailValid: false,
	   startValid: false,
	   endValid: false
    }
    this.handleClick = this.handleClick.bind(this);
	this.handleChange = this.handleChange.bind(this);
	this.addItem = this.addItem.bind(this);
  }
  handleClick() {
    console.log('Click happened');
  }
  addItem(){
	  const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: this.state.emailForPost,start: this.state.startForPost, end: this.state.endForPost })
    };
    fetch("/records", requestOptions)
            .then(response => {
                if(response.ok){
                    if (response.ok) {
                      return response.json();
                    } else {
                      this.setState({
                         error: response.statusText
                      });
                      console.log(response);
                    }
                }
            })
            .then(data => this.setState({ error: data+" Success " }));
      };
  handleChange(event) {
	  if(event.target.name==='emailForPost'){
		  this.validateEmail(event.target.value);
	  }
	  if(event.target.name==='startForPost'){
		  this.validateStartDate(event.target.value);
	  }
	  if(event.target.name==='endForPost'){
		  this.validateEndDate(event.target.value);
	  }


  }

  validateEmail(email){
	let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if ( re.test(email) ) {
        this.setState({
		  emailForPost: email,
		  emailValid: true,
		  errorForEmail: ""
	  });
    }
    else {
        this.setState({
			emailForPost: email,
		  emailValid: false,
		  errorForEmail: "Invalid Email. Please enter correct email format"
	  });
    }

  }
  validateStartDate(start){
	  console.log(start);
	let re = /^(0[1-9]|1[0-9]|2[0-9]|3[0-1]).(0[1-9]|1[0-2]).(20[\d]{2,2}) ([0-1][0-9]|2[0-3]):([0-5][0-9])$/;
    if ( re.test(start) ) {
        this.setState({
		  startForPost: start,
		  startValid: true,
		  errorForStart: ""
	  });
    }
    else {
        this.setState({
			startForPost: start,
		  startValid: false,
		  errorForStart: "Invalid Start Date. Please enter correct date format dd.mm.yyyy hh:mm"
	  });
    }

  }
  validateEndDate(end){
	  console.log(end);
	let re = /^(0[1-9]|1[0-9]|2[0-9]|3[0-1]).(0[1-9]|1[0-2]).(20[\d]{2,2}) ([0-1][0-9]|2[0-3]):([0-5][0-9])$/;
    if ( re.test(end) ) {
        this.setState({
		  endForPost: end,
		  endValid: true,
		  errorForEnd: ""
	  });
    }
    else {
        this.setState({
			endForPost: end,
		  endValid: false,
		  errorForEnd: "Invalid end Date. Please enter correct date format dd.mm.yyyy hh:mm"
	  });
    }

  }
  render() {
    return (
        <div>
		<h1>Add new Record</h1>
		<label>Email: <input type="email" value={this.state.emailForPost} name="emailForPost" onChange={this.handleChange} /></label><br/>
		<label>Start Date: <input type="text" value={this.state.startForPost} name="startForPost" onChange={this.handleChange} /></label><br/>
		<label>End: <input type="text" value={this.state.endForPost} name="endForPost" onChange={this.handleChange} /></label><br/>
        <button onClick={this.addItem} disabled= {!(this.state.emailValid && this.state.startValid && this.state.endValid)}>Add Record</button><br/>
		<div style= {{ color: 'red'}}>{this.state.errorForEmail}</div><br/>
		<div style= {{ color: 'red'}}>{this.state.errorForStart}</div>
		<div style= {{ color: 'red'}}>{this.state.errorForEnd}</div><br/>
		<div style= {{ color: 'red'}}>{this.state.error}</div><br/>
		   <hr />
        </div>

     );
  }
}

export default AddRecord;