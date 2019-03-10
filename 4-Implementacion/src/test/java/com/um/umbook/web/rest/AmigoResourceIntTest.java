package com.um.umbook.web.rest;

import com.um.umbook.UmbookApp;

import com.um.umbook.domain.Amigo;
import com.um.umbook.repository.AmigoRepository;
import com.um.umbook.repository.search.AmigoSearchRepository;
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
import java.util.Collections;
import java.util.List;


import static com.um.umbook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.um.umbook.domain.enumeration.Estado;
/**
 * Test class for the AmigoResource REST controller.
 *
 * @see AmigoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmbookApp.class)
public class AmigoResourceIntTest {

    private static final Estado DEFAULT_ESTADO = Estado.Aceptado;
    private static final Estado UPDATED_ESTADO = Estado.Rechazado;

    @Autowired
    private AmigoRepository amigoRepository;

    /**
     * This repository is mocked in the com.um.umbook.repository.search test package.
     *
     * @see com.um.umbook.repository.search.AmigoSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmigoSearchRepository mockAmigoSearchRepository;

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

    private MockMvc restAmigoMockMvc;

    private Amigo amigo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AmigoResource amigoResource = new AmigoResource(amigoRepository, mockAmigoSearchRepository);
        this.restAmigoMockMvc = MockMvcBuilders.standaloneSetup(amigoResource)
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
    public static Amigo createEntity(EntityManager em) {
        Amigo amigo = new Amigo()
            .estado(DEFAULT_ESTADO);
        return amigo;
    }

    @Before
    public void initTest() {
        amigo = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmigo() throws Exception {
        int databaseSizeBeforeCreate = amigoRepository.findAll().size();

        // Create the Amigo
        restAmigoMockMvc.perform(post("/api/amigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isCreated());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeCreate + 1);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the Amigo in Elasticsearch
        verify(mockAmigoSearchRepository, times(1)).save(testAmigo);
    }

    @Test
    @Transactional
    public void createAmigoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amigoRepository.findAll().size();

        // Create the Amigo with an existing ID
        amigo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmigoMockMvc.perform(post("/api/amigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Amigo in Elasticsearch
        verify(mockAmigoSearchRepository, times(0)).save(amigo);
    }

    @Test
    @Transactional
    public void getAllAmigos() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        // Get all the amigoList
        restAmigoMockMvc.perform(get("/api/amigos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
    
    @Test
    @Transactional
    public void getAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        // Get the amigo
        restAmigoMockMvc.perform(get("/api/amigos/{id}", amigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amigo.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAmigo() throws Exception {
        // Get the amigo
        restAmigoMockMvc.perform(get("/api/amigos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();

        // Update the amigo
        Amigo updatedAmigo = amigoRepository.findById(amigo.getId()).get();
        // Disconnect from session so that the updates on updatedAmigo are not directly saved in db
        em.detach(updatedAmigo);
        updatedAmigo
            .estado(UPDATED_ESTADO);

        restAmigoMockMvc.perform(put("/api/amigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAmigo)))
            .andExpect(status().isOk());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the Amigo in Elasticsearch
        verify(mockAmigoSearchRepository, times(1)).save(testAmigo);
    }

    @Test
    @Transactional
    public void updateNonExistingAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();

        // Create the Amigo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmigoMockMvc.perform(put("/api/amigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Amigo in Elasticsearch
        verify(mockAmigoSearchRepository, times(0)).save(amigo);
    }

    @Test
    @Transactional
    public void deleteAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeDelete = amigoRepository.findAll().size();

        // Delete the amigo
        restAmigoMockMvc.perform(delete("/api/amigos/{id}", amigo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Amigo in Elasticsearch
        verify(mockAmigoSearchRepository, times(1)).deleteById(amigo.getId());
    }

    @Test
    @Transactional
    public void searchAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);
        when(mockAmigoSearchRepository.search(queryStringQuery("id:" + amigo.getId())))
            .thenReturn(Collections.singletonList(amigo));
        // Search the amigo
        restAmigoMockMvc.perform(get("/api/_search/amigos?query=id:" + amigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amigo.class);
        Amigo amigo1 = new Amigo();
        amigo1.setId(1L);
        Amigo amigo2 = new Amigo();
        amigo2.setId(amigo1.getId());
        assertThat(amigo1).isEqualTo(amigo2);
        amigo2.setId(2L);
        assertThat(amigo1).isNotEqualTo(amigo2);
        amigo1.setId(null);
        assertThat(amigo1).isNotEqualTo(amigo2);
    }
}
