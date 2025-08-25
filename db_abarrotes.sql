CREATE TABLE categoria(
	idcat int primary key auto_increment,
    cat varchar(70) not null unique
)ENGINE=INNODB;

CREATE TABLE producto(
	idprod int primary key auto_increment,
    codigo varchar(20),
    prod varchar(100) not null,
    unidad varchar(70),
    stock double default 0,
    valor double default 0,
    descuento double default 0,
    img varchar(255),
    idcat int not null
)ENGINE=INNODB;
ALTER TABLE producto ADD CONSTRAINT pk_cat_prod 
FOREIGN KEY (idcat) REFERENCES categoria(idcat);

CREATE TABLE venta(
	nroventa int primary key auto_increment,
    fecha datetime default NOW(),
    totalventa double default 0,
    estado int default 0,
    medio_pago varchar(20)
)ENGINE=INNODB;

CREATE TABLE detalle(
	iddet int primary key auto_increment,
    cantidad double not null,
    totaldetalle double not null,
    nroventa int not null,
    idprod int not null
)ENGINE=INNODB;
ALTER TABLE detalle ADD CONSTRAINT pk_venta_detalle 
FOREIGN KEY (nroventa) REFERENCES venta(nroventa);
ALTER TABLE detalle ADD CONSTRAINT pk_prod_detalle 
FOREIGN KEY (idprod) REFERENCES producto(idprod);

DELIMITER $$
CREATE PROCEDURE getCategory(IN id INT, boolean_ BOOLEAN )
BEGIN
	IF boolean_ = TRUE THEN	
		SELECT * FROM categoria WHERE idcat = id;
	ELSEIF boolean_ = FALSE THEN
		SELECT * FROM categoria ORDER BY idcat ASC;
	END IF;
END $$
DELIMITER ;


DELIMITER //
CREATE PROCEDURE insertToCategory(IN category varchar(70))
BEGIN
	INSERT INTO categoria (cat) VALUES(category);
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE updateCategory(IN id_cat int, category varchar(70))
BEGIN
	UPDATE categoria
    SET cat = category
    WHERE idcat = id_cat;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE insertToProductMod2(
    IN cod_ VARCHAR(20),
    prod_ VARCHAR(100),
    unidad_ VARCHAR(70),
    stock_ DOUBLE,
    valor_ DOUBLE,
    desc_ DOUBLE,
    img_ VARCHAR(255),
    id_cat INT
)
BEGIN
    DECLARE codigoExistente VARCHAR(20);

    -- Paso 1: Valida si el código es NULL o una cadena vacía (trim es clave para evitar espacios en blanco)
    IF cod_ IS NULL OR TRIM(cod_) = '' THEN
        -- Si el código es NULL o vacío, procede directamente a la inserción
        INSERT INTO producto(codigo, prod, unidad, stock, valor, descuento, img, idcat)
        VALUES(cod_, prod_, unidad_, stock_, valor_, desc_, img_, id_cat);
    ELSE
        -- Paso 2: Si el código NO es NULL ni vacío, verifica si ya existe en la base de datos
        -- Usamos LIMIT 1 para asegurar que la consulta no falle si hay duplicados
        SELECT codigo INTO codigoExistente FROM producto WHERE codigo = cod_ LIMIT 1;
        
        -- Paso 3: Evalúa el resultado de la búsqueda
        IF codigoExistente IS NULL THEN
            -- Si no se encontró un código duplicado, realiza la inserción
            INSERT INTO producto(codigo, prod, unidad, stock, valor, descuento, img, idcat)
            VALUES(cod_, prod_, unidad_, stock_, valor_, desc_, img_, id_cat);
        ELSE
            -- Si el código ya existe, devuelve un mensaje de error
            SELECT 'El código del producto ya existe y no se ha realizado la inserción.';
        END IF;
    END IF;
END //
DELIMITER ;


DELIMITER //

CREATE PROCEDURE updateProductMod(
    IN id_prod INT,
    IN cod_ VARCHAR(20),
    IN prod_ VARCHAR(100),
    IN unidad_ VARCHAR(70),
    IN stock_ DOUBLE,
    IN valor_ DOUBLE,
    IN desc_ DOUBLE,
    IN img_ VARCHAR(255),
    IN id_cat INT
)
BEGIN
    DECLARE codigoExistente VARCHAR(20);

    -- Paso 1: Valida si el código a actualizar es NULL o una cadena vacía.
    -- Si es así, procede a la actualización directa.
    IF cod_ IS NULL OR TRIM(cod_) = '' THEN
        UPDATE producto
        SET
            -- **CAMBIO AQUÍ**: Ahora se actualiza explícitamente el campo 'codigo' con el valor vacío o NULL.
            codigo = cod_,
            prod = prod_,
            unidad = unidad_,
            stock = stock_,
            valor = valor_,
            descuento = desc_,
            img = img_,
            idcat = id_cat
        WHERE idprod = id_prod;
    ELSE
        -- Paso 2: Si el código NO es NULL ni vacío, verifica si ya existe en otro producto
        -- diferente al que se está actualizando.
        SELECT codigo INTO codigoExistente
        FROM producto
        WHERE codigo = cod_ AND idprod <> id_prod
        LIMIT 1;

        -- Paso 3: Evalúa el resultado de la búsqueda
        IF codigoExistente IS NULL THEN
            -- Si no se encontró un código duplicado, realiza la actualización.
            UPDATE producto
            SET
                codigo = cod_,
                prod = prod_,
                unidad = unidad_,
                stock = stock_,
                valor = valor_,
                descuento = desc_,
                img = img_,
                idcat = id_cat
            WHERE idprod = id_prod;
        ELSE
            -- Si el código ya existe en otro producto, devuelve un mensaje de error.
            SELECT 'El código del producto ya existe en otro registro y no se ha realizado la actualización.';
        END IF;
    END IF;
END //

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE `getProducts`(
    IN p_opcion INT,
    IN p_idCategoria INT
)
BEGIN
    IF p_opcion = 1 THEN
        -- Si p_opcion es 1, la consulta tiene LIMIT
        SELECT 
            idprod,
	    codigo,
            prod,
            unidad,
            stock,
            valor,
            descuento,
            img,
	    idcat,
            cat
        FROM producto JOIN categoria USING (idcat) ORDER BY idprod desc
        LIMIT 0, 60;
    ELSEIF p_opcion = 2 THEN
        -- Si p_opcion es 2, la consulta es sin LIMIT y con un WHERE
        SELECT 
            idprod,
            codigo,
            prod,
            unidad,
            stock,
            valor,
            descuento,
            img,
            idcat,
            cat
        FROM producto JOIN categoria USING (idcat)
        WHERE idcat = p_idCategoria ORDER BY idprod desc;
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE searchProduct(IN id_prod INT, IN cod VARCHAR(20), IN producto VARCHAR(100))
BEGIN
    IF id_prod > 0 AND cod = "" AND producto = "" THEN
        SELECT
            idprod, codigo, prod, unidad, stock, valor, descuento, img, idcat, cat
        FROM
            producto JOIN categoria USING(idcat)
        WHERE
            idprod = id_prod; -- Usamos el parámetro id_prod aquí
    
    ELSEIF id_prod = 0 AND cod != "" AND producto = "" THEN
        SELECT
            idprod, codigo, prod, unidad, stock, valor, descuento, img, idcat, cat
        FROM
            producto JOIN categoria USING(idcat)
        WHERE
            codigo = cod;
    
    ELSEIF id_prod = 0 AND cod = "" AND producto != "" THEN
        SELECT
            idprod, codigo, prod, unidad, stock, valor, descuento, img, idcat, cat
        FROM
            producto JOIN categoria USING(idcat)
        WHERE
            prod LIKE CONCAT(producto, '%'); -- Aquí está la corrección
            
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE insertSale(
    IN p_total DOUBLE, 
    IN p_mediopago VARCHAR(20),
    OUT p_id_venta INT
)
BEGIN
    -- Insertar los datos de la venta en la tabla
    INSERT INTO venta (totalventa, medio_pago) 
    VALUES (p_total, p_mediopago);

    -- Asignar el último ID generado a la variable de salida
    SET p_id_venta = LAST_INSERT_ID();
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE insertDetails(
    IN p_cantidad DOUBLE,
    IN p_total DOUBLE,
    IN p_prod VARCHAR(255),
    IN p_idventa INT
)
BEGIN
    -- Declarar la variable local para el ID del producto
    DECLARE v_idprod INT;
    
    -- Manejador de errores para revertir la transacción si algo sale mal
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error en la inserción de detalle. Transacción revertida.';
    END;

    -- Usar SELECT ... INTO para obtener el ID de forma segura
    -- Se usa INTO v_idprod para almacenar el resultado en la variable local
    SELECT idprod INTO v_idprod FROM producto WHERE prod = p_prod;
    
    -- Verificar si el producto se encontró. Si no se encontró, v_idprod será NULL.
    IF v_idprod IS NULL THEN
        -- Lanzar una señal de error si el producto no existe
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El producto especificado no fue encontrado.';
    ELSE
        -- Si el producto existe, proceder con la inserción
        INSERT INTO detalle (cantidad, totaldetalle, nroventa, idprod)
        VALUES (p_cantidad, p_total, p_idventa, v_idprod);
    END IF;
    
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE getSales(IN dateBefore VARCHAR(10), IN dateAfter VARCHAR(10))
BEGIN
	SELECT * FROM venta 
	WHERE fecha BETWEEN CONCAT(dateBefore, " 00:00:00") AND CONCAT(dateAfter, " 23:59:59")
	AND estado = 0;
END$$

DELIMITER ;


DELIMITER $$
CREATE PROCEDURE showSaleDetails(IN boleta INT)
BEGIN
	select iddet, codigo,prod,valor,descuento,cantidad,totaldetalle 
	from detalle left join venta using(nroventa)
			 left join producto using(idprod)
             where nroventa = boleta;
END$$
DELIMITER ;


COMMIT;