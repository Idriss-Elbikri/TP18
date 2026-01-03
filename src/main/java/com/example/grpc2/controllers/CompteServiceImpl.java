package com.example.grpc2.controllers;

import com.example.grpc2.entities.Compte;
import com.example.grpc2.entities.TypeCompteEntity;
import com.example.grpc2.services.CompteService;

import com.example.grpc2.stubs.CompteRequest;
import com.example.grpc2.stubs.CompteServiceGrpc;
import com.example.grpc2.stubs.GetAllComptesRequest;
import com.example.grpc2.stubs.GetAllComptesResponse;
import com.example.grpc2.stubs.SaveCompteRequest;
import com.example.grpc2.stubs.SaveCompteResponse;
import com.example.grpc2.stubs.TypeCompte;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {

    private final CompteService compteService;

    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }

    @Override
    public void allComptes(GetAllComptesRequest request,
                           StreamObserver<GetAllComptesResponse> responseObserver) {

        var comptesEntity = compteService.findAllComptes();

        GetAllComptesResponse.Builder resp = GetAllComptesResponse.newBuilder();
        for (Compte c : comptesEntity) {
            resp.addComptes(toProto(c));   // ✅ champ "comptes" dans ton proto
        }

        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request,
                           StreamObserver<SaveCompteResponse> responseObserver) {

        CompteRequest cr = request.getCompte();

        // ✅ Entity de ton projet (PAS ma.projet...)
        Compte entity = new Compte();
        entity.setSolde(cr.getSolde());
        entity.setDateCreation(cr.getDateCreation());
        entity.setType(toEntityEnum(cr.getType()));

        Compte saved = compteService.saveCompte(entity);

        SaveCompteResponse resp = SaveCompteResponse.newBuilder()
                .setCompte(toProto(saved))
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    // ===== Mapping Entity -> Proto =====
    private com.example.grpc2.stubs.Compte toProto(Compte e) {
        return com.example.grpc2.stubs.Compte.newBuilder()
                .setId(e.getId() == null ? "" : e.getId())
                .setSolde((float) e.getSolde())
                .setDateCreation(e.getDateCreation() == null ? "" : e.getDateCreation())
                .setType(toProtoEnum(e.getType()))
                .build();
    }

    private TypeCompte toProtoEnum(TypeCompteEntity t) {
        if (t == null) return TypeCompte.COURANT;
        return switch (t) {
            case COURANT -> TypeCompte.COURANT;
            case EPARGNE -> TypeCompte.EPARGNE;
        };
    }

    private TypeCompteEntity toEntityEnum(TypeCompte t) {
        return switch (t) {
            case EPARGNE -> TypeCompteEntity.EPARGNE;
            case COURANT, UNRECOGNIZED -> TypeCompteEntity.COURANT;
        };
    }
}
