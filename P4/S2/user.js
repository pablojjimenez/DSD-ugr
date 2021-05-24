
let serviceURL = document.URL;
let socket = io.connect(serviceURL);

document.getElementById('btn-check-outlined').onclick = () => {
    socket.emit('Aire-Acondicionado');
}

socket.on('valoresSensores', (valores) => {
    console.log(valores)
});