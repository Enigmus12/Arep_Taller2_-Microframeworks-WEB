function loadGetMsg() {
    let nameVar = document.getElementById("name").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById("getrespmsg").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/hello?name=" + nameVar);
    xhttp.send();
}

function loadPostMsg() {
    let nameVar = document.getElementById("postname").value;
    let url = "/hellopost?name=" + nameVar;

    fetch(url, {method: 'POST'})
        .then(x => x.text())
        .then(y => document.getElementById("postrespmsg").innerHTML = y);
}
