let MongoClient = require('mongodb').MongoClient;
const app = require('express')();
const http = require('http').Server(app);
let io = require("socket.io")(http);
let unirest = require("unirest");
let params = require('./params.json');

app.get('/', (req, res) => {
    res.sendFile('nuevo.html', {root: __dirname});
});


/**
 * Base de datos
 */
const COLECCION_NAME = 'sensores2dx40ss7';
MongoClient.connect("mongodb://localhost:27017/s2", {useNewUrlParser: true, useUnifiedTopology: true}, (err, db) => {
    if (err) throw err;
    let dbo = db.db("s2");
    let usuarios = [];

    dbo.createCollection(COLECCION_NAME, (err, collection) => {
        if (err) throw err;
        io.sockets.on('connection', (u) => {
            usuarios.push({address: u.request.connection.remoteAddress, port: u.request.connection.remotePort});
            obtenerRegistro();

            io.emit('usuarios-activos', usuarios);
            io.emit('datos-iniciales', params);

            u.on('modo-interactivo', () => {
                params.interactivo = !params.interactivo;
            });

            u.on('Aire-Acondicionado', () => {
                params.aire_acondicionado = !params.aire_acondicionado;
                let mensaje = "El usuario ha " + (params.aire_acondicionado ? "encendido" : "apagado") + " el aire acondicionado";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                //params.aire_acondicionado ? io.sockets.emit('encender', 'Aire-Acondicionado') : io.sockets.emit('apagar', 'Aire-Acondicionado');
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('persiana', () => {
                params.persiana = !params.persiana;
                let mensaje = "El usuario ha " + (params.persiana ? "abierto" : "cerrado") + " la persiana";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('ventana', () => {
                params.ventana = !params.ventana;
                let mensaje = "El usuario ha " + (params.ventana ? "abierto" : "cerrado") + " la ventana";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('aspirador', () => {
                params.aspirador = !params.aspirador;
                let mensaje = "El usuario ha " + (params.aspirador ? "abierto" : "cerrado") + " la ventana";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('humificador', () => {
                params.humificador = !params.humificador;
                let mensaje = "El usuario ha " + (params.humificador ? "encendido" : "apagado") + " el humificador";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('calefaccion', () => {
                params.calefaccion = !params.calefaccion;
                let mensaje = "El usuario ha " + (params.calefaccion ? "encendido" : "apagado") + " la calefacción";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('agente-domestico', () => {
                params.agente = !params.agente;
                let mensaje = "El usuario ha " + (params.agente ? "encendido" : "apagado") + " el agente";
                let salida = {
                    sms: mensaje,
                    tipo: 'string'
                }
                console.log('activado el agente ' + params.agente)
                actualizarValoresUsuarios(salida);
                obtenerRegistro() //Actualizamos el registro de la pagina de los sensores
            });

            u.on('valores-captados', (data) => {
                guardarDatosNuevos(data); // modifica el json que trabaja el server
                let mensajes = agente();
                let salida = {
                    sms: JSON.stringify(mensajes),
                    tipo: 'vectorjson' // para que el html sepa que salida procesar
                }
                actualizarValoresUsuarios(salida); // imprimimos mensajes del server

                // Inserto en mongo los datos nuevos.
                guardarEnMongo(data)


                obtenerRegistro();
            })

            u.on('disconnect', () => {
                console.log("El cliente " + u.request.connection.remoteAddress + " se va a desconectar");
                let pos = usuarios.indexOf({
                    address: u.request.connection.remoteAddress,
                    port: u.request.connection.remotePort
                })
                usuarios.splice(pos, 1);
                io.emit('usuarios-activos', usuarios);
            });

            function obtenerRegistro() {
                collection.find().toArray((err, results) => {
                    io.emit('obtener-registro', results);
                    if (err) alert(err)
                });
            }

            function guardarEnMongo(data) {
                collection.insertOne(data, {safe: true}, (err, result) => {
                    if (err) alert(err);
                });
            }
        });
    });
});

function actualizarValoresUsuarios(mensajes) {
    io.sockets.emit('mostrar-valor-sensores', mensajes);
}


function agente() {

    if (params.agente) {

        let mensajes = [];

        if (params.temp_act > params.temp_max && !params.aire_acondicionado) {
            params.aire_acondicionado = true;
            params.ventana = false;
            io.sockets.emit('click', 'Aire-Acondicionado')
            mensajes.push("El agente ha encendido el aire acondicionado. " + params.temp_act + " > " + params.temp_max);
            io.sockets.emit('click', 'ventana')
            mensajes.push("El agente ha cerrado la ventana porque hay algún dispositivo de control de temperatura en uso.");

            if (!params.humificador) {
                params.humificador = true;
                io.sockets.emit('click', 'humificador')
                mensajes.push("El agente ha encendido el humidificador porque el aire acondicionado está en uso.");
            }
            if (params.calefaccion) {
                params.calefaccion = false;
                io.sockets.emit('click', 'calefaccion')
                mensajes.push("El agente ha apagado la calefacción porque se ha encendido el aire acondicionado.");
            }

        } else if (params.temp_act < params.temp_min && params.aire_acondicionado) {
            params.aire_acondicionado = false;
            io.sockets.emit('click', 'Aire-Acondicionado')
            mensajes.push("El agente ha apagado el aire acondicionado porque la temperatura es inferior a " + params.temp_min);
        }


        if (params.temp_act < params.temp_min && !params.calefaccion) {
            params.calefaccion = true;
            io.sockets.emit('click', 'calefaccion')
            mensajes.push("El agente ha encendido la calefacción porque hace frío")
            if (params.aire_acondicionado) {
                params.aire_acondicionado = false;
                io.sockets.emit('click', 'Aire-Acondicionado')
                mensajes.push("El agente ha apagado el aire acondicionado para evitar tener el aire acondicionado y la calefacción encendidos simultáneamente");
            }
        } else if (params.temp_act > params.temp_min && params.calefaccion) {
            params.calefaccion = false;
            io.sockets.emit('click', 'calefaccion')
            mensajes.push("El agente ha apgado la calefacción porque ya no es necesaria");
        }

        if ((params.aire_acondicionado || params.calefaccion) && params.ventana) {
            params.ventana = false;
            io.sockets.emit('click', 'ventana')
            mensajes.push("El agente ha cerrado la ventana porque hay algún dispositivo de control de temperatura en uso.");
        } else if (!params.aire_acondicionado && !params.calefaccion && !params.ventana && params.vi_act < params.vi_max) {
            params.ventana = true;
            io.sockets.emit('click', 'ventana')
            mensajes.push("El agente ha abierto la ventana porque no hay ningún dispositivo de control de temperatura en uso.");
        }

        if (params.vi_act > params.vi_max && params.ventana) {
            params.ventana = false;
            io.sockets.emit('click', 'ventana')
            mensajes.push("El agente ha cerrado la ventana porque la velocidad del viento es muy alta.");
        }

        if (params.lum_act < params.lum_max && !params.persiana) {
            mensajes.push("El agente ha subido la persiana porque la luminosidad es inferior al umbral.");
            io.sockets.emit('click', 'persiana')
            params.persiana = true;
        }

        if (params.lum_act > params.lum_max && params.persiana) {
            params.persiana = false;
            io.sockets.emit('click', 'persiana')
            mensajes.push("El agente ha bajado la persiana porque la luminosidad es muy alta.");
        }

        if (params.suc_act > params.suc_max && !params.aspirador) {
            params.aspirador = true;
            io.sockets.emit('click', 'aspirador')
            mensajes.push("Se ha encendido el robot aspirador para quitar polvo.");

        } else if (params.suc_act <= params.suc_min && params.aspirador) {
            params.aspirador = false;
            io.sockets.emit('click', 'aspirador')
            mensajes.push("El robot aspirador ha terminado.")
        }

        if (params.hum_act > params.hum_max && !params.humificadr) {
            params.humificador = true;
            io.sockets.emit('click', 'humificador')
            mensajes.push("El agente ha encendido el humidificador porque los niveles de humedad están por encima del umbral.");

        } else if (params.hum_act <= params.hum_max && params.humificador && !params.aire_acondicionado) {
            params.humificador = false;
            io.sockets.emit('click', 'humificador')
            mensajes.push("El agente ha apagado el humidificador porque los niveles de humedad están en su intervalo.");
        }

        if(params.ventana && params.calefaccion) {
            params.ventana = false;
            io.sockets.emit('click', 'ventana')
        }

        return mensajes;
    }
}

function guardarDatosNuevos(data) {
    params.temp_max = data.temp_max;
    params.temp_min = data.temp_min;
    params.temp_act = data.temp_act;

    params.lum_max = data.lum_max;
    params.lum_min = data.lum_min;
    params.lum_act = data.lum_act;

    params.suc_max = data.suc_max;
    params.suc_min = data.suc_min;
    params.suc_act = data.suc_act;

    params.hum_max = data.hum_max;
    params.hum_min = data.hum_min;
    params.hum_act = data.hum_act;

    params.vi_max = data.vi_max;
    params.vi_min = data.vi_min;
    params.vi_act = data.vi_act;
}

http.listen(3000, () => {
    console.log('escuchando en *:3000');
});

setInterval(() => {
    peticionREST()
    if (params.interactivo) cambio()
}, 1000);

function cambio() {
    const TEMP = +1;
    const SUCIEDAD = +2;

    if (params.calefaccion) {
        let algo = params.temp_act + 1;
        params.temp_act = algo;
    }

    if (params.aire_acondicionado) {
        console.log('no1')
        params.temp_act = Number.parseInt(params.temp_act) - TEMP;
        params.hum_act = Number.parseInt(params.suc_act) + 1;
    }

    if (params.ventana) {
        console.log('no')
        params.temp_act = Number.parseInt(params.temp_act) + TEMP;
        params.suc_act = Number.parseInt(params.suc_act) + SUCIEDAD;
        params.vi_act = Number.parseInt(params.vi_act) + 1;
    } else {
        params.vi_act = Number.parseInt('0');
    }

    if (params.persiana) {
        params.lum_act = Number.parseInt(params.lum_act) + 1;
    } else {
        params.lum_act = params.lum_min;
    }

    if (params.humificador) {
        if(Number.parseInt(params.suc_act) > 1)
            params.hum_act = Number.parseInt(params.suc_act) - 1;
        else params.hum_act = 0;
    }

    if (params.aspirador) {
        if(Number.parseInt(params.suc_act) - 3 < 0) params.suc_act = 0;
        else params.suc_act = Number.parseInt(params.suc_act) - 3;
    }

    if (params.agente) {
        console.log('llamo al agente')
        let mensajes = agente();
        let salida = {
            sms: JSON.stringify(mensajes),
            tipo: 'vectorjson'
        }
        actualizarValoresUsuarios(salida);
    }

    // envio los datos cambiados
    io.sockets.emit('datos-iniciales', params);
}

function today() {
    let today = new Date();
    return "   " + today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
}

function peticionREST() {
    let req = unirest("GET", "https://api.coinbase.com/v2/exchange-rates?currency=BTC");
    req.end(function (res) {
        if (res.error) throw res.error;
        let envio = today() + ' ' + res.body.data.rates.EUR + 'EUR';
        io.emit('valor-moneda', envio);
    });
}