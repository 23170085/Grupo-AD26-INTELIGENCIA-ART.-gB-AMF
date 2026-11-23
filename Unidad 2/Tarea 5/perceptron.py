import numpy as np

# 1. Definir los datos de entrada (Tabla de verdad de la compuerta AND)
# Entradas: [x1, x2]
X = np.array([
    [0, 0],
    [0, 1],
    [1, 0],
    [1, 1]
])

# Salidas esperadas (y)
y = np.array([0, 0, 0, 1])

# 2. Inicializar parámetros
pesos = np.zeros(X.shape[1])  # Dos pesos iniciales en 0: [0.0, 0.0]
sesgo = 0.0                   # Bias (sesgo) inicial en 0
tasa_aprendizaje = 0.1        # Learning rate
epocas = 10                   # Número de iteraciones sobre todo el dataset

# Función de activación (Escalón unitario)
def activacion(suma_ponderada):
    return 1 if suma_ponderada >= 0 else 0

# 3. Entrenamiento del Perceptrón
print("Iniciando entrenamiento...")
for epoca in range(epocas):
    errores = 0
    for i in range(len(X)):
        # Cálculo de la suma ponderada: z = (x1*w1 + x2*w2) + b
        z = np.dot(X[i], pesos) + sesgo
        
        # Predicción
        prediccion = activacion(z)
        
        # Cálculo del error
        error = y[i] - prediccion
        
        # Actualización de pesos y sesgo si hubo error
        if error != 0:
            pesos += tasa_aprendizaje * error * X[i]
            sesgo += tasa_aprendizaje * error
            errores += 1
            
    print(f"Época {epoca + 1} - Errores: {errores} - Pesos: {pesos} - Sesgo: {sesgo:.2f}")
    
    # Si ya no hay errores, el perceptrón terminó de aprender
    if errores == 0:
        print("\n¡Entrenamiento completado con éxito!")
        break

# 4. Probar el modelo entrenado
print("\nResultados finales (Compuerta AND):")
for entrada in X:
    z = np.dot(entrada, pesos) + sesgo
    resultado = activacion(z)
    print(f"Entrada: {entrada} -> Predicción: {resultado}")