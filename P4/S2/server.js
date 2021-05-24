let MongoClient = require('mongodb').MongoClient;
let MongoServer = require('mongodb').Server;

const app = require('express')();
const http = require('http').Server(app);
let io = require("socket.io")(http);
let params = require('./params.json');

app.get('/', (req, res) => {
    res.sendFile('sensores.html', {root: __dirname});
});

app.get('/user', (req, res) => {
    res.sendFile('usuario.html', {root: __dirname});
});

/**
 * Base de datos
 */
MongoClient.connect("mongodb://localhost:27017/dbS2", {useNewUrlParser: true, useUnifiedTopology: true},(err, db) => {
    let dbo = db.db("dbS2");
    let usuarios = [];

    dbo.createCollection("registroCambios", (err, collection) => {
        io.sockets.on('connection', (u) => {
            usuarios.push({address: u.request.connection.remoteAddress, port: u.request.connection.remotePort});
            obtenerRegistro();

            io.emit('usuarios-activos', usuarios);


            u.on('Aire-Acondicionado', () => {
                params.aire_acondicionado = !params.aire_acondicionado;
                let mensaje = "El usuario ha " + (params.aire_acondicionado ? "encendido" : "apagado") + " el aire acondicionado";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('persiana', () => {
                params.persiana = !params.persiana;
                let mensaje = "El usuario ha " + (params.persiana ? "abierto" : "cerrado") + " la persiana";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('ventana', () => {
                params.ventana = !params.ventana;
                let mensaje = "El usuario ha " + (params.ventana ? "abierto" : "cerrado") + " la ventana";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('aspirador', () => {
                params.aspirador = !params.aspirador;
                let mensaje = "El usuario ha " + (params.aspirador ? "abierto" : "cerrado") + " la ventana";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('humificador', () => {
                params.humificador = !params.humificador;
                let mensaje = "El usuario ha " + (params.humificador ? "encendido" : "apagado") + " el humificador";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('calefaccion', () => {
                params.calefaccion = !params.calefaccion;
                let mensaje = "El usuario ha " + (params.calefaccion ? "encendido" : "apagado") + " la calefacción";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('agente-domestico', () => {
                params.agente = !params.agente;
                let mensaje = "El usuario ha " + (params.agente ? "encendido" : "apagado") + " el agente";
                actualizarValoresUsuarios(mensaje);
                //obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            function obtenerRegistro() {
                collection.find().toArray((err, results) => {
                    io.emit('obtener-registro',results);
                });
            }
        });
    });
} );

function actualizarValoresUsuarios(mensajes) {
    io.sockets.emit('mostrar-valor-sensores', mensajes);
}

http.listen(3000, function () {
    console.log('escuchando en *:3000');
});