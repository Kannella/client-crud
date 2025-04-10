package com.giokanella.clientscrud.services;

import com.giokanella.clientscrud.dto.ClientDTO;
import com.giokanella.clientscrud.entities.Client;
import com.giokanella.clientscrud.repositories.ClientRepository;
import com.giokanella.clientscrud.services.exceptions.DatabaseException;
import com.giokanella.clientscrud.services.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    //Update
    @Transactional
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

    //Delete
    @Transactional(propagation = Propagation.SUPPORTS) // Esse parametro so vai executar essa transacao (que nao eh so uma leitura) se esse metodo estiver no contexto de outra transao, se nao estiver nao precisa envolver com o Transactional e captura corretamente.
    public void delete(Long id) {

        if(!repository.existsById(id)) { //se nao existir o id que foi informado no banco
            throw new ResourcesNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    };

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
