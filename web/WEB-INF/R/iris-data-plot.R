
assign("t", plotType)
jpeg(imagePath,quality=90)
data(iris) 
attach(iris)
plot(Sepal.Length, Petal.Length, col=unclass(Species), type=t)
dev.off()