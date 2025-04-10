package com.giokanella.clientscrud.services;

import com.giokanella.clientscrud.dto.ClientDTO;
import com.giokanella.clientscrud.entities.Client;
import com.giokanella.clientscrud.repositories.ClientRepository;
import com.giokanella.clientscrud.services.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    //FindById
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = repository.findById(id).orElseThrow(() -> new ResourcesNotFoundException("Recurso não encontrado!"));
        return new ClientDTO(client);
    }

    //Insert
    @Transactional()
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();

        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());

        entity = repository.save(entity);

        return new ClientDTO(entity);

    }

    @Transactional // nao eh somente leitura agora
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = repository.getReferenceById(id);


            copyDtoToEntity(dto, entity);

            entity = repository.save(entity);

            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourcesNotFoundException("Recurso não encontrado");
        }
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
