package com.sk.elasticsearch.preservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.elasticsearch.entity.CategoryElasticsearch;
import com.sk.elasticsearch.repository.CategoryElasticsearchRepo;
import com.sk.elasticsearch.service.IndexService;
import com.sk.elasticserach.helper.Indices;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.CategoryDto;

@Service
public class CategoryElasticsearchService {
	@Autowired
	private CategoryElasticsearchRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IndexService service;

	public CategoryDto createEsCategory(CategoryDto categoryDto) {
		
		boolean b = service.isIndexAvailableOrNot(Indices.CATEGORY_INDEX);
		
		CategoryElasticsearch cat = this.modelMapper.map(categoryDto, CategoryElasticsearch.class);
		CategoryElasticsearch addedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(addedCat, CategoryDto.class);
	}

	public CategoryDto updateEsCategory(CategoryDto categoryDto, Integer categoryId) {

		CategoryElasticsearch cat = this.categoryRepo.findById(categoryId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "Category Id", categoryId));

		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());

		CategoryElasticsearch updatedcat = this.categoryRepo.save(cat);

		return this.modelMapper.map(updatedcat, CategoryDto.class);
	}

	public void deleteESCategory(Integer categoryId) {

		CategoryElasticsearch cat = this.categoryRepo.findById(categoryId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "category id", categoryId));
		this.categoryRepo.delete(cat);
	}

	public CategoryDto getEsCategory(Integer categoryId) {
		CategoryElasticsearch cat = this.categoryRepo.findById(categoryId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

		return this.modelMapper.map(cat, CategoryDto.class);
	}

	public List<CategoryDto> getEsCategories() {
		List<CategoryElasticsearch> list = new ArrayList<CategoryElasticsearch>();

		Iterable<CategoryElasticsearch> itr = this.categoryRepo.findAll();

		for (CategoryElasticsearch l : itr) {
			list.add(l);
		}

		List<CategoryDto> luserDto = list.stream().map((use) -> this.modelMapper.map(use, CategoryDto.class))
				.collect(Collectors.toList());

		return luserDto;
	}

}
