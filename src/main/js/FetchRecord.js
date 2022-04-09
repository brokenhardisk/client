import React from 'react';
class FetchRecord extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
       isLoaded: false,
       items: [],
	   emailForGet: "",
	   emailForGetValid: false,
	   errorForGet: "",
	   error: ""
    }
    this.handleClick = this.handleClick.bind(this);
	this.handleChange = this.handleChange.bind(this);
	this.fetchRemoteItems = this.fetchRemoteItems.bind(this);
  }
    fetchRemoteItems() {
       fetch("/records?email="+this.state.emailForGet)
          .then(response => {
                  if (response.ok) {
                    return response.json();
                  } else {
                    this.setState({
                       isLoaded: false,
                       error: response.statusText
                    });
                    console.log(response.statusText);
                  }
          })
          .then(
             (result) => {
                this.setItems(result);
             },
             (err) => {
                this.setState({
                   isLoaded: false,
                   error: err.message
                });
             }
          )
    };
  setItems(remoteItems) {
     var items = [];
     remoteItems.forEach((item) => {
        let newItem = {
           email: item.email,
           start: item.start,
           end: item.end,
        }
        items.push(newItem)
     });
     this.setState({
        isLoaded: true,
        items: items
     });
  }

  handleClick() {
    console.log('Click happened');
  }

  handleChange(event) {
	  let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  let email = event.target.value;

    if ( re.test(email) ) {
        this.setState({
		  emailForGet: event.target.value,
		  emailForGetValid: true,
		  errorForGet: ""
	  });
    }
    else {
        this.setState({
			emailForGet: event.target.value,
			emailForGetValid: false,
		  errorForGet: "Invalid Email. Please enter correct email format"
	  });
    }

  }
  render() {
     let lists = [];
     if (this.state.isLoaded) {
        lists = this.state.items.map((item) =>
           <tr onMouseEnter={this.handleMouseEnter} onMouseLeave={this.handleMouseLeave}>
              <td>{item.email}</td>
              <td>{new Date(item.start).toUTCString()}</td>
              <td>{new Date(item.end).toUTCString()}</td>
           </tr>
        );
     }
     return (
        <div>
        <h1>Fetch Records</h1>
		<input type="email" value={this.state.emailForGet} name="emailForGet" onChange={this.handleChange} />
        <button onClick={this.fetchRemoteItems} disabled= {!this.state.emailForGetValid}>Fetch Records</button>
		<div style= {{ color: 'red'}}>{this.state.errorForGet}</div>
		<div style= {{ color: 'red'}}>{this.state.err}</div>
           <table onMouseOver={this.handleMouseOver}>
              <thead>
                 <tr>
                    <th>Email</th>
                    <th>Start</th>
                    <th>End</th>
                 </tr>
              </thead>
              <tbody>
                 {lists}
              </tbody>
           </table>
		   <hr />
        </div>

     );
  }
}
export default FetchRecord;