package com.um.umbook.web.rest;

import com.um.umbook.UmbookApp;

import com.um.umbook.domain.Muro;
import com.um.umbook.repository.MuroRepository;
import com.um.umbook.repository.search.MuroSearchRepository;
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

/**
 * Test class for the MuroResource REST controller.
 *
 * @see MuroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmbookApp.class)
public class MuroResourceIntTest {

    @Autowired
    private MuroRepository muroRepository;

    /**
     * This repository is mocked in the com.um.umbook.repository.search test package.
     *
     * @see com.um.umbook.repository.search.MuroSearchRepositoryMockConfiguration
     */
    @Autowired
    private MuroSearchRepository mockMuroSearchRepository;

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

    private MockMvc restMuroMockMvc;

    private Muro muro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MuroResource muroResource = new MuroResource(muroRepository, mockMuroSearchRepository);
        this.restMuroMockMvc = MockMvcBuilders.standaloneSetup(muroResource)
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
    public static Muro createEntity(EntityManager em) {
        Muro muro = new Muro();
        return muro;
    }

    @Before
    public void initTest() {
        muro = createEntity(em);
    }

    @Test
    @Transactional
    public void createMuro() throws Exception {
        int databaseSizeBeforeCreate = muroRepository.findAll().size();

        // Create the Muro
        restMuroMockMvc.perform(post("/api/muros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muro)))
            .andExpect(status().isCreated());

        // Validate the Muro in the database
        List<Muro> muroList = muroRepository.findAll();
        assertThat(muroList).hasSize(databaseSizeBeforeCreate + 1);
        Muro testMuro = muroList.get(muroList.size() - 1);

        // Validate the Muro in Elasticsearch
        verify(mockMuroSearchRepository, times(1)).save(testMuro);
    }

    @Test
    @Transactional
    public void createMuroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = muroRepository.findAll().size();

        // Create the Muro with an existing ID
        muro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuroMockMvc.perform(post("/api/muros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muro)))
            .andExpect(status().isBadRequest());

        // Validate the Muro in the database
        List<Muro> muroList = muroRepository.findAll();
        assertThat(muroList).hasSize(databaseSizeBeforeCreate);

        // Validate the Muro in Elasticsearch
        verify(mockMuroSearchRepository, times(0)).save(muro);
    }

    @Test
    @Transactional
    public void getAllMuros() throws Exception {
        // Initialize the database
        muroRepository.saveAndFlush(muro);

        // Get all the muroList
        restMuroMockMvc.perform(get("/api/muros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muro.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getMuro() throws Exception {
        // Initialize the database
        muroRepository.saveAndFlush(muro);

        // Get the muro
        restMuroMockMvc.perform(get("/api/muros/{id}", muro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(muro.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMuro() throws Exception {
        // Get the muro
        restMuroMockMvc.perform(get("/api/muros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMuro() throws Exception {
        // Initialize the database
        muroRepository.saveAndFlush(muro);

        int databaseSizeBeforeUpdate = muroRepository.findAll().size();

        // Update the muro
        Muro updatedMuro = muroRepository.findById(muro.getId()).get();
        // Disconnect from session so that the updates on updatedMuro are not directly saved in db
        em.detach(updatedMuro);

        restMuroMockMvc.perform(put("/api/muros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMuro)))
            .andExpect(status().isOk());

        // Validate the Muro in the database
        List<Muro> muroList = muroRepository.findAll();
        assertThat(muroList).hasSize(databaseSizeBeforeUpdate);
        Muro testMuro = muroList.get(muroList.size() - 1);

        // Validate the Muro in Elasticsearch
        verify(mockMuroSearchRepository, times(1)).save(testMuro);
    }

    @Test
    @Transactional
    public void updateNonExistingMuro() throws Exception {
        int databaseSizeBeforeUpdate = muroRepository.findAll().size();

        // Create the Muro

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuroMockMvc.perform(put("/api/muros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(muro)))
            .andExpect(status().isBadRequest());

        // Validate the Muro in the database
        List<Muro> muroList = muroRepository.findAll();
        assertThat(muroList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Muro in Elasticsearch
        verify(mockMuroSearchRepository, times(0)).save(muro);
    }

    @Test
    @Transactional
    public void deleteMuro() throws Exception {
        // Initialize the database
        muroRepository.saveAndFlush(muro);

        int databaseSizeBeforeDelete = muroRepository.findAll().size();

        // Delete the muro
        restMuroMockMvc.perform(delete("/api/muros/{id}", muro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Muro> muroList = muroRepository.findAll();
        assertThat(muroList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Muro in Elasticsearch
        verify(mockMuroSearchRepository, times(1)).deleteById(muro.getId());
    }

    @Test
    @Transactional
    public void searchMuro() throws Exception {
        // Initialize the database
        muroRepository.saveAndFlush(muro);
        when(mockMuroSearchRepository.search(queryStringQuery("id:" + muro.getId())))
            .thenReturn(Collections.singletonList(muro));
        // Search the muro
        restMuroMockMvc.perform(get("/api/_search/muros?query=id:" + muro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muro.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Muro.class);
        Muro muro1 = new Muro();
        muro1.setId(1L);
        Muro muro2 = new Muro();
        muro2.setId(muro1.getId());
        assertThat(muro1).isEqualTo(muro2);
        muro2.setId(2L);
        assertThat(muro1).isNotEqualTo(muro2);
        muro1.setId(null);
        assertThat(muro1).isNotEqualTo(muro2);
    }
}
