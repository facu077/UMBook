package com.um.umbook.web.rest;

import com.um.umbook.UmbookApp;

import com.um.umbook.domain.Foto;
import com.um.umbook.repository.FotoRepository;
import com.um.umbook.repository.search.FotoSearchRepository;
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
import org.springframework.util.Base64Utils;
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

/**
 * Test class for the FotoResource REST controller.
 *
 * @see FotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmbookApp.class)
public class FotoResourceIntTest {

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    @Autowired
    private FotoRepository fotoRepository;

    /**
     * This repository is mocked in the com.um.umbook.repository.search test package.
     *
     * @see com.um.umbook.repository.search.FotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private FotoSearchRepository mockFotoSearchRepository;

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

    private MockMvc restFotoMockMvc;

    private Foto foto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FotoResource fotoResource = new FotoResource(fotoRepository, mockFotoSearchRepository);
        this.restFotoMockMvc = MockMvcBuilders.standaloneSetup(fotoResource)
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
    public static Foto createEntity(EntityManager em) {
        Foto foto = new Foto()
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE);
        return foto;
    }

    @Before
    public void initTest() {
        foto = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoto() throws Exception {
        int databaseSizeBeforeCreate = fotoRepository.findAll().size();

        // Create the Foto
        restFotoMockMvc.perform(post("/api/fotos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isCreated());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeCreate + 1);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testFoto.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);

        // Validate the Foto in Elasticsearch
        verify(mockFotoSearchRepository, times(1)).save(testFoto);
    }

    @Test
    @Transactional
    public void createFotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fotoRepository.findAll().size();

        // Create the Foto with an existing ID
        foto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFotoMockMvc.perform(post("/api/fotos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Foto in Elasticsearch
        verify(mockFotoSearchRepository, times(0)).save(foto);
    }

    @Test
    @Transactional
    public void getAllFotos() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        // Get all the fotoList
        restFotoMockMvc.perform(get("/api/fotos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foto.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }
    
    @Test
    @Transactional
    public void getFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        // Get the foto
        restFotoMockMvc.perform(get("/api/fotos/{id}", foto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foto.getId().intValue()))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)));
    }

    @Test
    @Transactional
    public void getNonExistingFoto() throws Exception {
        // Get the foto
        restFotoMockMvc.perform(get("/api/fotos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Update the foto
        Foto updatedFoto = fotoRepository.findById(foto.getId()).get();
        // Disconnect from session so that the updates on updatedFoto are not directly saved in db
        em.detach(updatedFoto);
        updatedFoto
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE);

        restFotoMockMvc.perform(put("/api/fotos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFoto)))
            .andExpect(status().isOk());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testFoto.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);

        // Validate the Foto in Elasticsearch
        verify(mockFotoSearchRepository, times(1)).save(testFoto);
    }

    @Test
    @Transactional
    public void updateNonExistingFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Create the Foto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotoMockMvc.perform(put("/api/fotos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Foto in Elasticsearch
        verify(mockFotoSearchRepository, times(0)).save(foto);
    }

    @Test
    @Transactional
    public void deleteFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeDelete = fotoRepository.findAll().size();

        // Delete the foto
        restFotoMockMvc.perform(delete("/api/fotos/{id}", foto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Foto in Elasticsearch
        verify(mockFotoSearchRepository, times(1)).deleteById(foto.getId());
    }

    @Test
    @Transactional
    public void searchFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);
        when(mockFotoSearchRepository.search(queryStringQuery("id:" + foto.getId())))
            .thenReturn(Collections.singletonList(foto));
        // Search the foto
        restFotoMockMvc.perform(get("/api/_search/fotos?query=id:" + foto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foto.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Foto.class);
        Foto foto1 = new Foto();
        foto1.setId(1L);
        Foto foto2 = new Foto();
        foto2.setId(foto1.getId());
        assertThat(foto1).isEqualTo(foto2);
        foto2.setId(2L);
        assertThat(foto1).isNotEqualTo(foto2);
        foto1.setId(null);
        assertThat(foto1).isNotEqualTo(foto2);
    }
}
