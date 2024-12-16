package com.uv.recuperatc.controller;

import com.uv.recuperatc.SupabaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private SupabaseClient supabaseClient;

    @GetMapping("/disponibles")
    public String getMateriasDisponibles(@RequestHeader("Authorization") String token) throws IOException {
        // Valida el token con Supabase
        supabaseClient.validateToken(token);
        return supabaseClient.getData("/rest/v1/materias_disponibles");
    }

    @GetMapping("/deseadas/{usuarioId}")
    public String getMateriasDeseadas(@RequestHeader("Authorization") String token, @PathVariable String usuarioId) throws IOException {
        // Valida el token con Supabase
        supabaseClient.validateToken(token);
        return supabaseClient.getData("/rest/v1/materias_deseadas?usuario_id=eq." + usuarioId);
    }

    @PostMapping("/deseadas/{usuarioId}/agregar")
    public void agregarMateriaDeseada(@RequestHeader("Authorization") String token, @PathVariable String usuarioId, @RequestBody Map<String, Object> requestBody) throws IOException {
        // Valida el token con Supabase
        supabaseClient.validateToken(token);
        String materiaId = (String) requestBody.get("materiaId");
        supabaseClient.updateData("/rest/v1/materias_deseadas?id=eq." + usuarioId, "materias_deseadas", materiaId, "add");
    }

    @PostMapping("/deseadas/{usuarioId}/eliminar")
    public void eliminarMateriaDeseada(@RequestHeader("Authorization") String token, @PathVariable String usuarioId, @RequestBody Map<String, Object> requestBody) throws IOException {
        // Valida el token con Supabase
        supabaseClient.validateToken(token);
        String materiaId = (String) requestBody.get("materiaId");
        supabaseClient.updateData("/rest/v1/materias_deseadas?id=eq." + usuarioId, "materias_deseadas", materiaId, "remove");
    }
}