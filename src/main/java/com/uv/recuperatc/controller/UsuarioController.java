package com.uv.recuperatc.controller;

import com.uv.recuperatc.SupabaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private SupabaseClient supabaseClient;

    @PostMapping("/registro")
    public String registrarUsuario(@RequestBody Map<String, Object> requestBody) {
        try {
            logger.info("Registro de usuario solicitado: {}", requestBody);
            
            // Extraer email y password del requestBody
            String email = (String) requestBody.get("email");
            String password = (String) requestBody.get("password");
            
            // Registrar al usuario
            Map<String, Object> signupData = new HashMap<>();
            signupData.put("email", email);
            signupData.put("password", password);
            
            String signupJson = new ObjectMapper().writeValueAsString(signupData);
            String signupResponse = supabaseClient.postData("/auth/v1/signup", signupJson);
            logger.info("Respuesta de Supabase al registrar usuario: {}", signupResponse);
            
            // Verificar si el registro fue exitoso y obtener el user_id del usuario registrado
            Map<String, Object> signupResponseMap = new ObjectMapper().readValue(signupResponse, Map.class);
            if (signupResponseMap.containsKey("user")) {
                Map<String, Object> user = (Map<String, Object>) signupResponseMap.get("user");
                String userId = (String) user.get("id"); // Obtener el user_id correctamente
                
                // Devolver el user_id al frontend
                Map<String, String> response = new HashMap<>();
                response.put("user_id", userId);
                return new ObjectMapper().writeValueAsString(response);
            } else {
                throw new RuntimeException("Error registrando usuario: no se encontró el campo 'user' en la respuesta");
            }
        } catch (IOException e) {
            logger.error("Error registrando usuario: ", e);
            throw new RuntimeException("Error registrando usuario", e);
        }
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestBody Map<String, Object> requestBody) {
        try {
            logger.info("Inicio de sesión solicitado para: {}", requestBody);
            String json = new ObjectMapper().writeValueAsString(requestBody);
            logger.info("Payload JSON para Supabase: {}", json);
            String response = supabaseClient.postData("/auth/v1/token?grant_type=password", json);
            logger.info("Respuesta de Supabase: {}", response);
            return response;
        } catch (IOException e) {
            logger.error("Error en el inicio de sesión: ", e);
            throw new RuntimeException("Error en el inicio de sesión", e);
        }
    }

    @GetMapping("/perfil")
    public String obtenerPerfilUsuario(@RequestHeader("Authorization") String token) {
        try {
            logger.info("Solicitud de perfil de usuario con token: {}", token);
            supabaseClient.validateToken(token);
            String response = supabaseClient.getData("/auth/v1/user");
            logger.info("Respuesta de Supabase: {}", response);
            return response;
        } catch (IOException e) {
            logger.error("Error obteniendo perfil de usuario: ", e);
            throw new RuntimeException("Error obteniendo perfil de usuario", e);
        }
    }

    @PutMapping("/actualizar")
    public String actualizarUsuario(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody) {
        try {
            logger.info("Actualización de usuario solicitada con token: {}", token);
            supabaseClient.validateToken(token);
            String json = new ObjectMapper().writeValueAsString(requestBody);
            logger.info("Payload JSON para Supabase: {}", json);
            String response = supabaseClient.postData("/auth/v1/user", json);
            logger.info("Respuesta de Supabase: {}", response);
            return response;
        } catch (IOException e) {
            logger.error("Error actualizando usuario: ", e);
            throw new RuntimeException("Error actualizando usuario", e);
        }
    }
}