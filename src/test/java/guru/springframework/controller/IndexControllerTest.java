package guru.springframework.controller;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }


    @Test
    public void testMockMVC() throws Exception  {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(view().name("Index"));
    }

    @Test
    public void getIndexPage() {

        //given
        Set<Recipe> recipeSet = new HashSet<>();

        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);

        recipeSet.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipeSet.add(recipe2);

        //when
        when(recipeService.getRecipes()).thenReturn(recipeSet);

        ArgumentCaptor<Set<Recipe>>  argumentCaptor = ArgumentCaptor.forClass(Set.class);

        String indexPageValue = "Index";
        String indexPageReturnValue = indexController.getIndexPage(model);

        assertEquals(indexPageValue,indexPageReturnValue);
        verify(recipeService,times(1)).getRecipes();

        verify(model,times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());

        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(setInController.size(),recipeSet.size());
    }
}