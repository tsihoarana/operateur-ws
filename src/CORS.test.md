## Test CORS avec jquery
```javascript
$.ajax({
    url: "http://localhost:5000/client",
    type: "GET",
    headers: { 
        "Accept" : "application/json; charset=utf-8",
        "Content-Type": "application/json; charset=utf-8",
        "Authorization" : "Bearer affec8b7f8e22f8dab4a8ef7dfde6fdf6a272485"
    },
    data: { numero : "0340000001", password: "1234" },
    dataType:"json"
}).done(data => {
    console.log(data);
});
```
## Test POST
```javascript
$.ajax({
    url: "http://localhost:5000/client/connexion",
    type: "POST",
    headers: { 
        "Content-Type": "application/json; charset=utf-8"
    },
    data : JSON.stringify({ numero : "0340000001", password: "124" }),
    dataType:"json"
}).done(data => {
    console.log(data);
});
```