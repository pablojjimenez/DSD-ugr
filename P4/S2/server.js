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
MongoClient.connect("mongodb://localhost:27017/s2", {useNewUrlParser: true, useUnifiedTopology: true}, (err, db) => {
    if (err) throw err;
    let dbo = db.db("s2");
    let usuarios = [];

    dbo.createCollection("registroCambios", (err, collection) => {
        if(err) throw err;
        io.sockets.on('connection', (u) => {
            usuarios.push({address: u.request.connection.remoteAddress, port: u.request.connection.remotePort});
            obtenerRegistro();

            io.emit('usuarios-activos', usuarios);


            u.on('Aire-Acondicionado', () => {
                params.aire_acondicionado = !params.aire_acondicionado;
                let mensaje = "El usuario ha " + (params.aire_acondicionado ? "encendido" : "apagado") + " el aire acondicionado";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('persiana', () => {
                params.persiana = !params.persiana;
                let mensaje = "El usuario ha " + (params.persiana ? "abierto" : "cerrado") + " la persiana";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('ventana', () => {
                params.ventana = !params.ventana;
                let mensaje = "El usuario ha " + (params.ventana ? "abierto" : "cerrado") + " la ventana";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('aspirador', () => {
                params.aspirador = !params.aspirador;
                let mensaje = "El usuario ha " + (params.aspirador ? "abierto" : "cerrado") + " la ventana";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('humificador', () => {
                params.humificador = !params.humificador;
                let mensaje = "El usuario ha " + (params.humificador ? "encendido" : "apagado") + " el humificador";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('calefaccion', () => {
                params.calefaccion = !params.calefaccion;
                let mensaje = "El usuario ha " + (params.calefaccion ? "encendido" : "apagado") + " la calefacción";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('agente-domestico', () => {
                params.agente = !params.agente;
                let mensaje = "El usuario ha " + (params.agente ? "encendido" : "apagado") + " el agente";
                actualizarValoresUsuarios(mensaje);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('valores-captados', (data) => {
                switch (data.sensor) {
                    case 'Temperatura': params.temp_actual = data.valor; break;
                    case 'Luminosidad': params.lum_actual = data.valor;  break;
                    case 'Suciedad': params.suciedad = data.valor;       break;
                    case 'Humedad': params.humedad = data.valor;         break;
                }
                let mensajes = agente();
                console.log(mensajes)
                actualizarValoresUsuarios(mensajes);
                collection.insertOne(data, {safe:true}, (err, result) => {});
                obtenerRegistro();
            })

            u.on('disconnect',() => {
                console.log("El cliente " + u.request.connection.remoteAddress + " se va a desconectar");
                let pos = usuarios.indexOf({address:u.request.connection.remoteAddress, port:u.request.connection.remotePort})
                usuarios.splice(pos, 1);
                io.emit('usuarios-activos', usuarios);
            });

            function obtenerRegistro() {
                collection.find().toArray((err, results) => {
                    if(err) throw err;
                    io.emit('obtener-registro', results);
                });
            }
        });
    });
});

function actualizarValoresUsuarios(mensajes) {
    io.sockets.emit('mostrar-valor-sensores', mensajes);
}

function agente()
{
    if(params.agenteDomotico)
    {
        let mensajes = [];

        if (params.temp_actual > params.MAX_TEMP && !params.aire_acondicionado)
        {
            params.aire_acondicionado = true;
            mensajes.push("El agente ha encendido el aire acondicionado. " + params.temp_actual + " > " + params.MAX_TEMP);
            if(!params.humidificador) {
                params.humificador = true;
                mensajes.push("El agente ha encendido el humidificador porque el aire acondicionado está en uso.");
            }
            if(params.calefaccion) {
                params.calefaccion = false;
                mensajes.push("El agente ha apagado la calefacción porque se ha encendido el aire acondicionado.");
            }

        } else if (params.temp_actual < params.MAX_TEMP && params.temp_actual > params.MIN_TEMP && params.aire_acondicionado) {
            params.aire_acondicionado = false;
            mensajes.push("El agente ha apagado el aire acondicionado porque la temperatura es inferior a " + params.MIN_TEMP);
        }

        if(params.temp_actual < params.MIN_TEMP && !params.calefaccion) {
            params.calefaccion = true;
            mensajes.push("El agente ha encendido la calefacción porque hace frío")
            if(params.aire_acondicionado) {
                params.aireAcondicionado = false;
                mensajes.push("El agente ha apagado el aire acondicionado para evitar tener el aire acondicionado y la calefacción encendidos simultáneamente");
            }
        }else if (params.temp_actual > params.MIN_TEMP && params.calefaccion) {
            params.calefaccion = false;
            mensajes.push("El agente ha apgado la calefacción porque ya no es necesaria");
        }

        if((params.aireAcondicionado || params.calefaccion) && params.ventana) {
            params.ventana = false;
            mensajes.push("El agente ha cerrado la ventana porque hay algún dispositivo de control de temperatura en uso.");
        }else if (!params.aire_acondicionado && !params.calefaccion && !params.ventana && params.viento_actual < params.MAX_VIENTO) {
            params.ventana = true;
            mensajes.push("El agente ha abierto la ventana porque no hay ningún dispositivo de control de temperatura en uso.");
        }

        if(params.viento_actual > params.MAX_VIENTO && params.ventana) {
            params.ventana = false;
            mensajes.push("El agente ha cerrado la ventana porque la velocidad del viento es muy alta.");
        }

        if (params.lum_actual < params.MAX_LUZ && !params.persiana) {
            mensajes.push("El agente ha subido la persiana porque la luminosidad es inferior al umbral.");
            params.persiana = true;
        }

        if (params.temp_actual > params.MAX_TEMP && params.lum_actual > params.MAX_LUZ && params.persiana) {
            params.persiana = false;
            mensajes.push("El agente ha bajado la persiana porque la temperatura y la luminosidad son muy altas.");
        }

        if(params.suciedad > params.UMBRAL_SUCIEDAD && !params.aspirador) {
            params.aspirador = true;
            mensajes.push("Se ha encendido el robot aspirador para quitar polvo.");

        }else if (params.suciedad < params.UMBRAL_SUCIEDAD && params.aspirador) {
            params.aspirador = false;
            mensajes.push("El robot aspirador a terminado.")
        }

        if(params.humedad < params.UMBRAL_HUMEDAD && !params.humificador) {
            params.humificador = true;
            mensajes.push("El agente ha encendido el humidificador porque los niveles de humedad están por debajo del umbral.");

        } else if(params.humedad > params.UMBRAL_HUMEDAD && params.humificador && !params.aire_acondicionado) {
            params.humificador = false;
            mensajes.push("El agente ha apagado el humidificador porque los niveles de humedad están por encima del umbral y el aire acondicionado está apagado.");
        }

        return mensajes;
    }
}

http.listen(3000, function () {
    console.log('escuchando en *:3000');
});