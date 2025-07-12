package com.skinslabs.back.repository;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.model.CasoUso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CasoUsoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CasoUsoRepository casoUsoRepository;

    @Test
    void testSaveAndFind() {
        // Crear un BotServicio
        BotServicio botServicio = new BotServicio();
        botServicio.setTitulo("Bot de Prueba");
        botServicio.setDescripcion("Descripción de prueba");
        entityManager.persistAndFlush(botServicio);

        // Crear un CasoUso
        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion("Caso de uso de prueba");
        casoUso.setBotServicio(botServicio);
        
        CasoUso saved = casoUsoRepository.save(casoUso);
        
        assertNotNull(saved.getId());
        assertEquals("Caso de uso de prueba", saved.getDescripcion());
    }

    @Test
    void testFindByBotServicio() {
        // Crear un BotServicio
        BotServicio botServicio = new BotServicio();
        botServicio.setTitulo("Bot de Prueba");
        botServicio.setDescripcion("Descripción de prueba");
        entityManager.persistAndFlush(botServicio);

        // Crear casos de uso
        CasoUso casoUso1 = new CasoUso();
        casoUso1.setDescripcion("Caso de uso 1");
        casoUso1.setBotServicio(botServicio);
        casoUsoRepository.save(casoUso1);

        CasoUso casoUso2 = new CasoUso();
        casoUso2.setDescripcion("Caso de uso 2");
        casoUso2.setBotServicio(botServicio);
        casoUsoRepository.save(casoUso2);

        List<CasoUso> casos = casoUsoRepository.findAll();
        
        assertEquals(2, casos.size());
    }

    @Test
    void testDelete() {
        // Crear un BotServicio
        BotServicio botServicio = new BotServicio();
        botServicio.setTitulo("Bot de Prueba");
        botServicio.setDescripcion("Descripción de prueba");
        entityManager.persistAndFlush(botServicio);

        // Crear un CasoUso
        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion("Caso de uso a eliminar");
        casoUso.setBotServicio(botServicio);
        CasoUso saved = casoUsoRepository.save(casoUso);

        casoUsoRepository.delete(saved);
        
        assertFalse(casoUsoRepository.findById(saved.getId()).isPresent());
    }
} 