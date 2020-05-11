create database if not exists contabil;

create table if not exists usuario(
    codigo bigint(15) unsigned not null auto_increment primary key,
    nome varchar(75) not null,
    sobrenome varchar(75),
    congelado bit default 0
);

create table if not exists conta(
    codigo bigint(15) unsigned not null auto_increment primary key ,
    codigo_usuario bigint(15) unsigned not null,
    numero varchar(75) not null,
    nome varchar(75) not null,
    codigo_conta_pai bigint(15) unsigned,
    descricao text(500),
    constraint conta_usuario_unique unique(codigo, codigo_usuario),
    constraint conta_pai_fk foreign key(codigo_conta_pai) references conta(codigo),
    constraint conta_usuario_fk foreign key(codigo_usuario) references usuario(codigo)
);

create table if not exists lancamento(
    codigo bigint(15) unsigned not null auto_increment primary key,
    codigo_usuario bigint(15) unsigned not null,
    data date not null,
    historico varchar(75) not null
);

create table if not exists valor(
    codigo bigint(15) unsigned not null auto_increment primary key,
    conta_codigo bigint(15) unsigned not null,
    lancamento_codigo bigint(15) unsigned not null,
    tipo char(7) not null,
    valor float(18,2) not null,
    constraint valor_conta_fk foreign key(conta_codigo) references conta(codigo),
    constraint valor_lancamento_fk foreign key(lancamento_codigo) references lancamento(codigo)
);