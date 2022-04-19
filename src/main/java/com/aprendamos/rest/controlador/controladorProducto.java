package com.aprendamos.rest.controlador;

import com.aprendamos.rest.entidades.producto;
import com.aprendamos.rest.repositorio.repositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class controladorProducto {
    @Autowired
    repositorioProducto crud;
    @GetMapping("/productos/{id}")
    public ResponseEntity<producto> obtenerProducto(@PathVariable("id") Integer id){
        Optional<producto> datoProducto = crud.findById(id);
        if(datoProducto.isPresent()){
            return new ResponseEntity<>(datoProducto.get(), HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/productos")
    public ResponseEntity<List<producto>> obtenerTodos(){
        try {
            List<producto> productos = new ArrayList<producto>();
            crud.findAll().forEach(productos::add);
            if (productos.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return  new ResponseEntity<>(productos,HttpStatus.OK);
        }catch (Exception e){
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/buscarnombre")
    public ResponseEntity<List<producto>> buscarNombre(@RequestParam(required = false) String nombre){
        try {
            List<producto> productos = new ArrayList<producto>();
            crud.findByNombreContaining(nombre).forEach(productos::add);
            if(productos.isEmpty()){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return  new ResponseEntity<>(productos, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/crearproducto")
    public ResponseEntity<producto> crearProducto(@RequestBody producto nuevoProducto){
        try {
            producto _producto = crud.save(new producto(nuevoProducto.getNombre(), nuevoProducto.getPrecio(), nuevoProducto.getDescripcion()));
            return  new ResponseEntity<>(_producto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/actualizarproducto/{id}")
    public ResponseEntity<producto> actualizarProducto(@PathVariable("id") Integer id, @RequestBody producto productoActualizar){
        try {
            Optional<producto> datoProducto = crud.findById(id);
            if(datoProducto.isPresent()){
                producto _producto = datoProducto.get();
                _producto.setNombre(productoActualizar.getNombre());
                _producto.setPrecio(productoActualizar.getPrecio());
                _producto.setDescripcion(productoActualizar.getDescripcion());
                return new ResponseEntity<>(crud.save(_producto),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("eliminarproducto/{id}")
    public ResponseEntity<HttpStatus> eliminarProducto(@PathVariable("id") Integer id){
        try {
            crud.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/eliminarproductos")
    public ResponseEntity<HttpStatus> eliminarTodo(){
        try {
            crud.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
