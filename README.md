=======================================================================
Notes
Bagian Admin
> (Done)

Bagian Cashier
>>> Untuk Receipt kita ngambil dari Order yang saat itu juga Statusnya diganti menjadi PAID --> dibuat receipt dan dimasukan ke db receipt. (Done)

Bagian Barista / Chef
> (Done)

Bagian Waiter
> (Done)

Bagian StockManager
> Untuk Stock akan di lakukan secara manual. Jadi selesai per hari akan di timbang satu per satu dan di update oleh StockManager itu sendiri apa yang kurang dan apa yang cukup. (Sudah aman sepertinya) (Done)
>>> Setiap Pesanan akan mengurangi bahan dari inventory yang ada dan pengurangan tersebut akan masuk kedalam inventory_logs.
	JIKA bahan di inventory itu tidak cukup untuk membuat sebuah makanan/minuman dari Order_item, maka item yang tidak cukup bahannya 	itu tidak bsia dipesan / di disable, akan ada lagi jika bahan sudah ada Kembali. (Done)


-- Smart SQL --
Mengetahui status dalam Order dan Order_Items yang berguna untuk UpdateOnSpot (Tidak perlu Refresh). Dipakai Didalam Cheff/Barista
DELIMITER $$

DROP TRIGGER IF EXISTS auto_update_order_status$$

CREATE TRIGGER auto_update_order_status
AFTER UPDATE ON order_items
FOR EACH ROW
BEGIN
    DECLARE total_active INT;
    DECLARE done_active INT;
    DECLARE served_active INT;
    
    -- 1. Hitung HANYA item yang TIDAK CANCELED (Item Aktif)
    SELECT COUNT(*) INTO total_active 
    FROM order_items WHERE order_id = NEW.order_id AND status != 'CANCELED';
    
    -- 2. Hitung item Aktif yang sudah selesai masak (DONE/SERVED)
    SELECT COUNT(*) INTO done_active 
    FROM order_items WHERE order_id = NEW.order_id 
    AND status != 'CANCELED' AND (status = 'DONE' OR status = 'SERVED');
    
    -- 3. Hitung item Aktif yang sudah diantar (SERVED)
    SELECT COUNT(*) INTO served_active 
    FROM order_items WHERE order_id = NEW.order_id 
    AND status != 'CANCELED' AND status = 'SERVED';

    -- LOGIKA DONE: Jika semua item aktif sudah selesai
    -- (Pastikan ada item aktif minimal 1, supaya order kosong tidak dianggap Done)
    IF (total_active > 0 AND total_active = done_active) THEN
        UPDATE orders SET status = 'DONE' 
        WHERE order_id = NEW.order_id AND status != 'FULLY_SERVED';
    END IF;

    -- LOGIKA FULLY_SERVED: Jika semua item aktif sudah diantar
    IF (total_active > 0 AND total_active = served_active) THEN
        UPDATE orders SET status = 'FULLY_SERVED' 
        WHERE order_id = NEW.order_id;
    END IF;
    
END$$

DELIMITER ;

