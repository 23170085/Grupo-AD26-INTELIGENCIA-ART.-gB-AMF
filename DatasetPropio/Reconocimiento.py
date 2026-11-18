import cv2
import numpy as np
import tensorflow as tf
import json
import os

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(SCRIPT_DIR, "mejor_modelo.keras")
CLASSES_PATH = os.path.join(SCRIPT_DIR, "clases.json")

# Cargar nuestro modelo entrenado
model = tf.keras.models.load_model(MODEL_PATH)

# Cargar los nombres de las personas
with open(CLASSES_PATH, "r", encoding="utf-8") as archivo:
    class_names = json.load(archivo)

# Detector de rostros Haar Cascade
detector = cv2.CascadeClassifier(
    cv2.data.haarcascades + "haarcascade_frontalface_default.xml"
)

# Abrir cámara web
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    raise RuntimeError("No se pudo acceder a la cámara web")

while True:

    ret, frame = cap.read()

    if not ret:
        break

    # Convertir imagen a escala de grises
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detectar rostros
    faces = detector.detectMultiScale(
        gray,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(60, 60)
    )

    for (x, y, w, h) in faces:

        # Recortar el rostro
        face = frame[y:y+h, x:x+w]

        # Convertir de BGR a RGB
        face = cv2.cvtColor(face, cv2.COLOR_BGR2RGB)

        # Redimensionar a 128 x 128
        face = cv2.resize(face, (128, 128))

        # Preparar imagen para la CNN
        face = face.astype("float32")
        face = np.expand_dims(face, axis=0)

        # Obtener predicciones
        predictions = model.predict(face, verbose=0)[0]

        # Obtener la clase con mayor probabilidad
        index = np.argmax(predictions)

        confidence = float(predictions[index])

        # Umbral inicial de reconocimiento
        threshold = 0.65

        if confidence >= threshold:
            name = class_names[index]
            color = (0, 255, 0)
        else:
            name = "Desconocido"
            color = (0, 0, 255)

        # Mostrar cuadro alrededor del rostro
        cv2.rectangle(
            frame,
            (x, y),
            (x+w, y+h),
            color,
            2
        )

        # Mostrar nombre y puntuación
        text = f"{name}: {confidence * 100:.1f}%"

        cv2.putText(
            frame,
            text,
            (x, max(y-10, 20)),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.7,
            color,
            2
        )

    # Mostrar resultado
    cv2.imshow("Reconocimiento Facial CNN", frame)

    # Salir presionando Q
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

cap.release()
cv2.destroyAllWindows()