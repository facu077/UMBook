package com.um.umbook.web.rest;

import com.um.umbook.UmbookApp;

import com.um.umbook.domain.Mensaje;
import com.um.umbook.repository.MensajeRepository;
import com.um.umbook.repository.search.MensajeSearchRepository;
import com.um.umbook.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.um.umbook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MensajeResource REST controller.
 *
 * @see MensajeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmbookApp.class)
public class MensajeResourceIntTest {

    private static final String DEFAULT_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MensajeRepository mensajeRepository;

    /**
     * This repository is mocked in the com.um.umbook.repository.search test package.
     *
     * @see com.um.umbook.repository.search.MensajeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MensajeSearchRepository mockMensajeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMensajeMockMvc;

    private Mensaje mensaje;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MensajeResource mensajeResource = new MensajeResource(mensajeRepository, mockMensajeSearchRepository);
        this.restMensajeMockMvc = MockMvcBuilders.standaloneSetup(mensajeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mensaje createEntity(EntityManager em) {
        Mensaje mensaje = new Mensaje()
            .texto(DEFAULT_TEXTO)
            .fecha(DEFAULT_FECHA);
        return mensaje;
    }

    @Before
    public void initTest() {
        mensaje = createEntity(em);
    }

    @Test
    @Transactional
    public void createMensaje() throws Exception {
        int databaseSizeBeforeCreate = mensajeRepository.findAll().size();

        // Create the Mensaje
        restMensajeMockMvc.perform(post("/api/mensajes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensaje)))
            .andExpect(status().isCreated());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeCreate + 1);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(DEFAULT_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(DEFAULT_FECHA);

        // Validate the Mensaje in Elasticsearch
        verify(mockMensajeSearchRepository, times(1)).save(testMensaje);
    }

    @Test
    @Transactional
    public void createMensajeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mensajeRepository.findAll().size();

        // Create the Mensaje with an existing ID
        mensaje.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensajeMockMvc.perform(post("/api/mensajes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensaje)))
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Mensaje in Elasticsearch
        verify(mockMensajeSearchRepository, times(0)).save(mensaje);
    }

    @Test
    @Transactional
    public void getAllMensajes() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList
        restMensajeMockMvc.perform(get("/api/mensajes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensaje.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }
    
    @Test
    @Transactional
    public void getMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get the mensaje
        restMensajeMockMvc.perform(get("/api/mensajes/{id}", mensaje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mensaje.getId().intValue()))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMensaje() throws Exception {
        // Get the mensaje
        restMensajeMockMvc.perform(get("/api/mensajes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Update the mensaje
        Mensaje updatedMensaje = mensajeRepository.findById(mensaje.getId()).get();
        // Disconnect from session so that the updates on updatedMensaje are not directly saved in db
        em.detach(updatedMensaje);
        updatedMensaje
            .texto(UPDATED_TEXTO)
            .fecha(UPDATED_FECHA);

        restMensajeMockMvc.perform(put("/api/mensajes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMensaje)))
            .andExpect(status().isOk());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(UPDATED_FECHA);

        // Validate the Mensaje in Elasticsearch
        verify(mockMensajeSearchRepository, times(1)).save(testMensaje);
    }

    @Test
    @Transactional
    public void updateNonExistingMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Create the Mensaje

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensajeMockMvc.perform(put("/api/mensajes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensaje)))
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mensaje in Elasticsearch
        verify(mockMensajeSearchRepository, times(0)).save(mensaje);
    }

    @Test
    @Transactional
    public void deleteMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeDelete = mensajeRepository.findAll().size();

        // Delete the mensaje
        restMensajeMockMvc.perform(delete("/api/mensajes/{id}", mensaje.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Mensaje in Elasticsearch
        verify(mockMensajeSearchRepository, times(1)).deleteById(mensaje.getId());
    }

    @Test
    @Transactional
    public void searchMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);
        when(mockMensajeSearchRepository.search(queryStringQuery("id:" + mensaje.getId())))
            .thenReturn(Collections.singletonList(mensaje));
        // Search the mensaje
        restMensajeMockMvc.perform(get("/api/_search/mensajes?query=id:" + mensaje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensaje.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mensaje.class);
        Mensaje mensaje1 = new Mensaje();
        mensaje1.setId(1L);
        Mensaje mensaje2 = new Mensaje();
        mensaje2.setId(mensaje1.getId());
        assertThat(mensaje1).isEqualTo(mensaje2);
        mensaje2.setId(2L);
        assertThat(mensaje1).isNotEqualTo(mensaje2);
        mensaje1.setId(null);
        assertThat(mensaje1).isNotEqualTo(mensaje2);
    }
}
